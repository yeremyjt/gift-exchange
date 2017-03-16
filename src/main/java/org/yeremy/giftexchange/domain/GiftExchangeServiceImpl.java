package org.yeremy.giftexchange.domain;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.NotAllowedException;

import org.springframework.transaction.annotation.Transactional;
import org.yeremy.giftexchange.dao.ExchangeHistoryDao;
import org.yeremy.giftexchange.dao.PersonDao;
import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;
import org.yeremy.giftexchange.dto.Person;
import org.yeremy.giftexchange.dto.PersonType;

@Named
@Singleton
public class GiftExchangeServiceImpl implements GiftExchangeService
{
    @Inject
    private PersonDao personDao;

    @Inject
    private ExchangeHistoryDao exchangeHistoryDao;

    @Override
    @Transactional
    public List<GiftSet> getGiftExchangeList(String familyGroup, Boolean record)
    {
        familyGroup = familyGroup.toUpperCase();

        int thisYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear();
        List<ExchangeHistory> thisYearExchangeHistory = exchangeHistoryDao.getExchangeHistory(familyGroup, thisYear);

        if (thisYearExchangeHistory.size() > 0 && record)
        {
            throw new NotAllowedException("The gift exchange for the year " + thisYear + " has already been recorded.");
        }

        int currentYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear();

        int oneYearAgo = currentYear - 1;
        int twoYearsAgo = currentYear - 2;
        int threeYearsAgo = currentYear - 3;
        int fourYearsAgo = currentYear - 4;
        int fiveYearsAgo = currentYear - 5;

        List<ExchangeHistory> previousYearsExchangeHistories = new ArrayList<>();
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                oneYearAgo));
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                twoYearsAgo));
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                threeYearsAgo));
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                fourYearsAgo));
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                fiveYearsAgo));

        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> persons = personDao.getPersons(familyGroup);

        giftSets.addAll(assignGiftSets(persons, PersonType.PARENT, previousYearsExchangeHistories));
        // giftSets.addAll(assignGiftSets(persons, PersonType.CHILD, previousYearsExchangeHistories));

        // check(giftSets, persons);
        //
        // if (record)
        // {
        // recordExchangeHistory(familyGroup, giftSets);
        // }

        return giftSets;
    }

    private void check(List<GiftSet> giftSets, List<Person> persons)
    {
        for (Person person : persons)
        {
            boolean found = false;
            for (GiftSet giftSet : giftSets)
            {
                if (giftSet.getGiver().equals(person))
                {
                    found = true;
                }
            }

            if (!found)
            {
                throw new RuntimeException(person.getName() + " wasn't found as a giver.");
            }

            found = false;
            for (GiftSet giftSet : giftSets)
            {
                if (giftSet.getGiver().equals(person))
                {
                    found = true;
                }
            }

            if (!found)
            {
                throw new RuntimeException(person.getName() + " wasn't found as a receiver.");
            }
        }
    }

    @Override
    public List<ExchangeHistory> getExchangeHistory(String familyGroup, int year)
    {
        familyGroup = familyGroup.toUpperCase();
        return exchangeHistoryDao.getExchangeHistory(familyGroup, year);
    }

    private void recordExchangeHistory(String familyGroup, List<GiftSet> giftSets)
    {
        for (GiftSet giftSet : giftSets)
        {
            ExchangeHistory exchangeHistory = new ExchangeHistory();
            exchangeHistory.setGiverId(giftSet.getGiver().getId());
            exchangeHistory.setGiverName(giftSet.getGiver().getName());
            exchangeHistory.setReceiverId(giftSet.getReceiver().getId());
            exchangeHistory.setReceiverName(giftSet.getReceiver().getName());
            exchangeHistory.setFamilyGroup(familyGroup);
            exchangeHistory.setYear(OffsetDateTime.now(ZoneId.of("UTC")).getYear());
            exchangeHistoryDao.writeExchangeHistory(exchangeHistory);
        }
    }

    private List<Person> randomizeReceivers(List<Person> persons)
    {
        long seed = System.nanoTime();
        Collections.shuffle(persons, new Random(seed));

        return persons;
    }

    private List<GiftSet> assignGiftSets(List<Person> persons, PersonType type,
            List<ExchangeHistory> previousYearsExchangeHistories)
    {
        final List<Person> givers = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());
        List<Person> potentialReceivers = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());
        // potentialReceivers = randomizeReceivers(potentialReceivers);
        List<Person> potentialReceiversReverseOrder = reverseOrder(potentialReceivers);
        final List<Person> receivers = new ArrayList<>();

        List<GiftSet> giftSets = new ArrayList<>();

        for (Person giver : givers)
        {
            Person receiver = null;
            GiftSet giftSet = new GiftSet();
            giftSet.setGiver(giver);

            for (Person potentialReceiver : potentialReceiversReverseOrder)
            {
                if (potentialReceiver.getName().equals(giver.getName())
                        || potentialReceiver.getFamilyName().equals(giver.getFamilyName())
                        || receivers.contains(potentialReceiver)
                        || sameAsPreviousYear(giver, potentialReceiver, previousYearsExchangeHistories))
                {
                    continue;
                }
                receiver = potentialReceiver;
                receivers.add(potentialReceiver);
                giftSet.setReceiver(receiver);
                giftSets.add(giftSet);
                break;
            }
        }

        return giftSets;
    }

    private List<Person> reverseOrder(List<Person> potentialReceivers)
    {
        List<Person> persons = new ArrayList<>();

        int n = potentialReceivers.size();

        for (int i = n - 1; i >= 0; i--)
        {
            persons.add(potentialReceivers.get(i));
        }

        return persons;
    }

    private boolean sameAsPreviousYear(Person giver, Person potentialReceiver,
            List<ExchangeHistory> previousYearsExchangeHistories)
    {
        ExchangeHistory match = previousYearsExchangeHistories.stream()
                .filter(x -> x.getGiverId() == giver.getId())
                .filter(x -> x.getReceiverId() == potentialReceiver.getId())
                .findAny()
                .orElse(null);

        if (match == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
