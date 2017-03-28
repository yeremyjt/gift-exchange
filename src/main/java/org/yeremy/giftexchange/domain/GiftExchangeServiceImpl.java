package org.yeremy.giftexchange.domain;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throws FileNotFoundException, UnsupportedEncodingException
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
            throws FileNotFoundException, UnsupportedEncodingException
    {
        final List<GiftSet> giftSets = new ArrayList<>();
        final List<Person> filteredPersons = persons.stream().filter(x -> x.getType() == type)
                .collect(Collectors.toList());
        final List<Person> reverseOrderFilteredPersons = getListInReverseOrder(filteredPersons);
        List<Person> potentialReceivers = new ArrayList<>(filteredPersons);

        final int n = filteredPersons.size();

        final Map<Person, List<Person>> possibleAssignmentsMap = new HashMap<>();
        final Map<Person, Person> assignmentsMap = new HashMap<>();

        potentialReceivers = randomizeReceivers(potentialReceivers);
        // Setting list of potential receivers for each giver.
        for (final Person giver : filteredPersons)
        {
            final List<Person> potentialReceiversForGiver = new ArrayList<>(potentialReceivers);
            potentialReceiversForGiver.remove(giver);
            potentialReceiversForGiver.remove(getSpouse(giver, potentialReceivers));
            potentialReceiversForGiver
                    .removeAll(getPriorYearsReceiversByGiver(giver, previousYearsExchangeHistories));

            possibleAssignmentsMap.put(giver, potentialReceiversForGiver);
        }

        final PrintWriter writer = new PrintWriter("/home/yeremy/possible-assignments.txt", "UTF-8");

        for (final Person person : filteredPersons)
        {
            writer.print(person.toString() + " -> ");

            for (final Person p : possibleAssignmentsMap.get(person))
            {
                writer.print(" - ");
                writer.print(p.toString());
            }
            writer.println();
        }
        writer.close();

        computeAssignments(filteredPersons, possibleAssignmentsMap, assignmentsMap);

        for (final Person giver : filteredPersons)
        {
            final GiftSet giftSet = new GiftSet();
            giftSet.setGiver(giver);
            giftSet.setReceiver(assignmentsMap.get(giver));
            giftSets.add(giftSet);
        }

        return giftSets;
    }

    private boolean checkAssignmentsMap(Map<Person, Person> assignmentsMap, List<Person> filteredPersons)
    {
        for (final Person person : filteredPersons)
        {
            if (!assignmentsMap.containsKey(person) || !assignmentsMap.containsValue(person))
            {
                return false;
            }
        }

        return true;
    }

    private List<Person> getListInReverseOrder(List<Person> filteredPersons)
    {
        final List<Person> reverseOrderList = new ArrayList<>();
        final int n = filteredPersons.size() - 1;

        for (int i = n; i >= 0; i--)
        {
            reverseOrderList.add(filteredPersons.get(i));
        }

        return reverseOrderList;
    }

    private void computeAssignments(List<Person> persons, Map<Person, List<Person>> possibleAssignmentsMap,
            Map<Person, Person> assignmentsMap)
    {
        for (final Person giver : persons)
        {
            final List<Person> potentialReceivers = possibleAssignmentsMap.get(giver);

            for (final Person receiver : potentialReceivers)
            {
                if (!assignmentsMap.containsValue(receiver))
                {
                    assignmentsMap.put(giver, receiver);
                    break;
                }
            }
        }

        List<Person> missingGivers = getMissingGivers(assignmentsMap, persons);
        while (missingGivers.size() > 0)
        {
            swap(assignmentsMap, possibleAssignmentsMap, persons, missingGivers);
            missingGivers = getMissingGivers(assignmentsMap, persons);
        }
    }

    private void swap(Map<Person, Person> assignmentsMap, Map<Person, List<Person>> possibleAssignmentsMap,
            List<Person> persons, List<Person> missingGivers)
    {
        final List<Person> missingReceivers = getMissingReceivers(assignmentsMap, persons);

        if (missingReceivers.size() > 0)
        {
            // Match one missing giver with one missing receiver
            final Person missingGiver = missingGivers.iterator().next();
            final Person missingReceiver = missingReceivers.iterator().next();

            // Find a match and swap
            for (final Person giver : persons)
            {
                if (possibleAssignmentsMap.get(giver).contains(missingReceiver))
                {
                    if (assignmentsMap.containsKey(giver))
                    {
                        if (possibleAssignmentsMap.get(missingGiver).contains(assignmentsMap.get(giver)))
                        {
                            final Person receiverToSwap = assignmentsMap.get(giver);
                            assignmentsMap.replace(giver, missingReceiver);
                            assignmentsMap.put(missingGiver, receiverToSwap);
                            return;
                        }
                    }
                }
            }
        }

    }

    private List<Person> getMissingGivers(Map<Person, Person> assignmentsMap, List<Person> persons)
    {
        final List<Person> missingGivers = new ArrayList<>();

        for (final Person person : persons)
        {
            if (!assignmentsMap.containsKey(person))
            {
                missingGivers.add(person);
            }
        }

        return missingGivers;
    }

    private List<Person> getMissingReceivers(Map<Person, Person> assignmentsMap, List<Person> persons)
    {
        final List<Person> missingReceivers = new ArrayList<>();

        for (final Person person : persons)
        {
            if (!assignmentsMap.containsValue(person))
            {
                missingReceivers.add(person);
            }
        }

        return missingReceivers;
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
