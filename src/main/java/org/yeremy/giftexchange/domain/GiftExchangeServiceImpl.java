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
    private PersonDao personDao;

    @Override
    public List<GiftSet> getGiftExchangeList(String familyGroup)
    {
        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> persons = personDao.getPersons(familyGroup);

        giftSets.addAll(assignGiftSets(persons, PersonType.PARENT));
        giftSets.addAll(assignGiftSets(persons, PersonType.CHILD));

        return giftSets;
    }

    private List<Person> randomizeReceivers(List<Person> persons)
    {
        long seed = System.nanoTime();
        Collections.shuffle(persons, new Random(seed));

        return persons;
    }

    private List<GiftSet> assignGiftSets(List<Person> persons, PersonType type)
    {
        final List<Person> givers = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());

        while (true)
        {
            List<Person> potentialReceivers = persons.stream().filter(x -> x.getType() == type)
                    .collect(Collectors.toList());
            potentialReceivers = randomizeReceivers(potentialReceivers);
            List<GiftSet> giftSets = new ArrayList<>();
            boolean colition = false;

            for (Person giver : givers)
            {
                Person receiver = null;
                GiftSet giftSet = new GiftSet();
                giftSet.setGiver(giver);

                for (Person potentialReceiver : potentialReceivers)
                {
                    if (potentialReceiver.getName()
                            .equals(giver.getName()) || potentialReceiver.getFamilyName().equals(giver.getFamilyName()))
                    {
                        colition = true;
                        break;
                    }
                    receiver = potentialReceiver;
                    giftSet.setReceiver(receiver);
                    giftSets.add(giftSet);
                    break;
                }

                if (colition)
                {
                    break;
                }

                potentialReceivers.remove(receiver);
            }

            if (colition)
            {
                continue;
            }
            else
            {
                return giftSets;
            }
        }
    }
}
