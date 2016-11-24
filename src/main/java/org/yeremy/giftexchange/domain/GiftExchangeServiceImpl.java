package org.yeremy.giftexchange.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yeremy.giftexchange.dao.PersonDao;
import org.yeremy.giftexchange.dto.GiftSet;

@Named
@Singleton
public class GiftExchangeServiceImpl implements GiftExchangeService
{
    @Inject
    PersonDao personDao;

    @Override
    public List<GiftSet> getGiftExchangeList(String familyGroup)
    {
        List<GiftSet> giftSets = new ArrayList<>();
        List<Person> persons = personDao.getPersons(familyGroup);

        List<Person> parentGivers = persons.stream().filter(x -> x.getType() == PersonType.PARENT)
                .collect(Collectors.toList());
        List<Person> potentialParentReceivers = persons.stream().filter(x -> x.getType() == PersonType.PARENT)
                .collect(Collectors.toList());
        List<Person> childrenGivers = persons.stream().filter(x -> x.getType() == PersonType.CHILD)
                .collect(Collectors.toList());
        List<Person> potentialChildrenReceivers = persons.stream().filter(x -> x.getType() == PersonType.CHILD)
                .collect(Collectors.toList());

        for (Person parent : parentGivers)
        {
            Person receiver = null;
            GiftSet giftSet = new GiftSet();
            giftSet.setGiver(parent);

            randomizeReceivers(potentialParentReceivers);

            for (Person parentReceiver : potentialParentReceivers)
            {
                if (parentReceiver.getName()
                        .equals(parent.getName()) || parentReceiver.getFamilyName().equals(parent.getFamilyName()))
                {
                    randomizeReceivers(potentialParentReceivers);
                    continue;
                }
                receiver = parentReceiver;
                giftSet.setReceiver(parentReceiver);
                giftSets.add(giftSet);
                break;
            }

            potentialParentReceivers.remove(receiver);
        }

        for (Person child : childrenGivers)
        {
            Person receiver = null;
            GiftSet giftSet = new GiftSet();
            giftSet.setGiver(child);

            randomizeReceivers(potentialChildrenReceivers);

            for (Person childReceiver : potentialChildrenReceivers)
            {
                if (childReceiver.getName()
                        .equals(child.getName()) || childReceiver.getFamilyName().equals(child.getFamilyName()))
                {
                    randomizeReceivers(potentialChildrenReceivers);
                    continue;
                }
                receiver = childReceiver;
                giftSet.setReceiver(childReceiver);
                giftSets.add(giftSet);
                break;
            }

            potentialChildrenReceivers.remove(receiver);
        }

        return giftSets;
    }

    private List<Person> randomizeReceivers(List<Person> persons)
    {
        long seed = System.nanoTime();
        Collections.shuffle(persons, new Random(seed));

        return persons;
    }
}
