package org.yeremy.giftexchange.dao;

import java.util.List;

import org.yeremy.giftexchange.dto.Person;

public interface PersonDao
{
    List<Person> getPersons(String familyGroup);
}
