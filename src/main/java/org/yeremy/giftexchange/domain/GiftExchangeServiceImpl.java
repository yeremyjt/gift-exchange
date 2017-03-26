package org.yeremy.giftexchange.domain;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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

        final int thisYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear();
        final List<ExchangeHistory> thisYearExchangeHistory = exchangeHistoryDao.getExchangeHistory(familyGroup,
                thisYear);

        if (thisYearExchangeHistory.size() > 0 && record)
        {
            throw new NotAllowedException("The gift exchange for the year " + thisYear + " has already been recorded.");
        }

        final int currentYear = OffsetDateTime.now(ZoneId.of("UTC")).getYear();

        final int oneYearAgo = currentYear - 1;
        // int twoYearsAgo = currentYear - 2;

        final List<ExchangeHistory> previousYearsExchangeHistories = new ArrayList<>();
        previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
                oneYearAgo));
        // previousYearsExchangeHistories.addAll(exchangeHistoryDao.getExchangeHistory(familyGroup,
        // twoYearsAgo));

        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> persons = personDao.getPersons(familyGroup);

        giftSets.addAll(assignGiftSets(persons, PersonType.PARENT, previousYearsExchangeHistories, true));
        // giftSets.addAll(assignGiftSets(persons, PersonType.CHILD, previousYearsExchangeHistories, false));

        check(giftSets, persons, PersonType.PARENT);
        // check(giftSets, persons, PersonType.CHILD);

        if (record)
        {
            recordExchangeHistory(familyGroup, giftSets);
        }

        return giftSets;
    }

    private void check(List<GiftSet> giftSets, List<Person> persons, PersonType personType)
    {
        List<Person> personsToCheck;
        if (personType != null)
        {
            personsToCheck = persons.stream().filter(x -> x.getType() == personType)
                    .collect(Collectors.toList());
        }
        else
        {
            personsToCheck = persons;
        }

        for (final Person person : personsToCheck)
        {
            boolean found = false;
            for (final GiftSet giftSet : giftSets)
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
            for (final GiftSet giftSet : giftSets)
            {
                if (giftSet.getReceiver().equals(person))
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
        for (final GiftSet giftSet : giftSets)
        {
            final ExchangeHistory exchangeHistory = new ExchangeHistory();
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
        final long seed = System.nanoTime();
        Collections.shuffle(persons, new Random(seed));

        return persons;
    }

    private List<GiftSet> assignGiftSets(List<Person> persons, PersonType type,
            List<ExchangeHistory> previousYearsExchangeHistories, boolean checkPriorYears)
    {
        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> givers = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());
        final List<Person> receivers = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());
        List<Person> potentialReceivers = new ArrayList<>(receivers);
        potentialReceivers = randomizeReceivers(potentialReceivers);

        final int n = givers.size();

        final Map<Person, List<Person>> possibleAssignmentsMap = new HashMap<>();
        final Map<Person, Person> assignmentsMap = new HashMap<>();

        // Setting list of potential receivers for each giver.
        for (final Person giver : givers)
        {
            final List<Person> potentialReceiversForGiver = new ArrayList<>(potentialReceivers);
            potentialReceiversForGiver.remove(giver);
            potentialReceiversForGiver.remove(getSpouse(giver, potentialReceivers));
            potentialReceiversForGiver.removeAll(getPriorYearsReceiversByGiver(giver, previousYearsExchangeHistories));

            possibleAssignmentsMap.put(giver, potentialReceiversForGiver);
        }

        computeAssignments(receivers, possibleAssignmentsMap, assignmentsMap);

        for (final Person giver : givers)
        {
            final GiftSet giftSet = new GiftSet();
            giftSet.setGiver(giver);
            giftSet.setReceiver(assignmentsMap.get(giver));
            giftSets.add(giftSet);
        }

        return giftSets;
    }

    private void computeAssignments(List<Person> receivers, Map<Person, List<Person>> possibleAssignmentsMap,
            Map<Person, Person> assignmentsMap)
    {
        for (final Person receiver : receivers)
        {
            final Set<Person> giversSet = possibleAssignmentsMap.keySet();

            for (final Person giver : giversSet)
            {
                if (possibleAssignmentsMap.get(giver).contains(receiver))
                {
                    if (!assignmentsMap.containsKey(giver))
                    {
                        assignmentsMap.put(giver, receiver);
                        break;
                    }
                    else
                    {
                        continue;
                    }
                }
            }
        }

        final List<Person> leftOutReceivers = getLeftOutReceivers(assignmentsMap, receivers);

        computeAssignmentsForLeftOut(leftOutReceivers, receivers, possibleAssignmentsMap, assignmentsMap);

    }

    private void computeAssignmentsForLeftOut(List<Person> leftOutReceivers, List<Person> receivers,
            Map<Person, List<Person>> possibleAssignmentsMap, Map<Person, Person> assignmentsMap)
    {
        for (final Person receiver : leftOutReceivers)
        {
            final Set<Person> giversSet = possibleAssignmentsMap.keySet();

            for (final Person giver : giversSet)
            {
                if (possibleAssignmentsMap.get(giver).contains(receiver))
                {
                    final Person oldReceiver = assignmentsMap.get(giver);
                    assignmentsMap.replace(giver, receiver);
                    possibleAssignmentsMap.get(giver).remove(oldReceiver);
                    break;
                }
            }
        }

        final List<Person> leftOutReceiversInThisRound = getLeftOutReceivers(assignmentsMap, receivers);

        if (leftOutReceiversInThisRound.size() == 0)
            return;

        computeAssignmentsForLeftOut(leftOutReceiversInThisRound, receivers, possibleAssignmentsMap, assignmentsMap);

    }

    private List<Person> getLeftOutReceivers(Map<Person, Person> assignmentsMap, List<Person> receivers)
    {
        final List<Person> leftOutReceivers = new ArrayList<>();

        for (final Person receiver : receivers)
        {
            if (!assignmentsMap.containsValue(receiver))
            {
                leftOutReceivers.add(receiver);
            }
        }

        return leftOutReceivers;
    }

    private List<Person> reverseOrder(List<Person> potentialReceivers)
    {
        final List<Person> persons = new ArrayList<>();

        final int n = potentialReceivers.size();

        for (int i = n - 1; i >= 0; i--)
        {
            persons.add(potentialReceivers.get(i));
        }

        return persons;
    }

    private boolean sameAsPreviousYear(Person giver, Person potentialReceiver,
            List<ExchangeHistory> previousYearsExchangeHistories)
    {
        final ExchangeHistory match = previousYearsExchangeHistories.stream()
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

    private List<Person> getPriorYearsReceiversByGiver(Person giver,
            List<ExchangeHistory> previousYearsExchangeHistories)
    {
        final List<ExchangeHistory> matches = previousYearsExchangeHistories.stream()
                .filter(x -> x.getGiverId() == giver.getId())
                .collect(Collectors.toList());

        final List<Person> receivers = new ArrayList<>();

        for (final ExchangeHistory exchangeHistory : matches)
        {
            receivers.add(personDao.getPersonById(exchangeHistory.getReceiverId()));
        }

        return receivers;
    }

    private Person getSpouse(Person giver, List<Person> potentialReceiversForGiver)
    {
        final Person spouse = potentialReceiversForGiver.stream()
                .filter(x -> x.getFamilyName().equals(giver.getFamilyName()) && x.getId() != giver.getId())
                .findAny()
                .orElse(null);

        return spouse;
    }
}
