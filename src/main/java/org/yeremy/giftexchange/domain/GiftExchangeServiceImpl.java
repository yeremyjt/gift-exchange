package org.yeremy.giftexchange.domain;

import org.springframework.transaction.annotation.Transactional;
import org.yeremy.giftexchange.dao.ExchangeHistoryDao;
import org.yeremy.giftexchange.dao.PersonDao;
import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;
import org.yeremy.giftexchange.dto.Person;
import org.yeremy.giftexchange.dto.PersonType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.NotAllowedException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
        int thisYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear();
        List<ExchangeHistory> thisYearExchangeHistory = exchangeHistoryDao.getExchangeHistory(familyGroup, thisYear);

        if (thisYearExchangeHistory.size() > 0 && record)
        {
            throw new NotAllowedException("The gift exchange for the year " + thisYear + " has already been recorded.");
        }

        int lastYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear() - 1;
        List<ExchangeHistory> lastYearExchangeHistory = exchangeHistoryDao.getExchangeHistory(familyGroup, lastYear);

        familyGroup = familyGroup.toUpperCase();
        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> persons = personDao.getPersons(familyGroup);

        giftSets.addAll(assignGiftSets(persons, PersonType.PARENT, lastYearExchangeHistory));
        giftSets.addAll(assignGiftSets(persons, PersonType.CHILD, lastYearExchangeHistory));

        if (record)
        {
            recordExchangeHistory(familyGroup, giftSets);
        }
        
        return giftSets;
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

    private List<GiftSet> assignGiftSets(List<Person> persons, PersonType type, List<ExchangeHistory> lastYearExchangeHistory)
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
                            .equals(giver.getName()) || potentialReceiver.getFamilyName().equals(giver.getFamilyName())
                            || sameAsLastYear(giver, potentialReceiver, lastYearExchangeHistory))
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

    private boolean sameAsLastYear(Person giver, Person potentialReceiver, List<ExchangeHistory> lastYearExchangeHistory)
    {

        ExchangeHistory lastYearGiver;
        try
        {
            lastYearGiver = lastYearExchangeHistory.stream().filter(x -> x.getGiverId() == giver.getId()).findFirst().get();
        }
        catch (NoSuchElementException e)
        {
            return false;
        }


        if (lastYearGiver.getReceiverId() == potentialReceiver.getId())
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
