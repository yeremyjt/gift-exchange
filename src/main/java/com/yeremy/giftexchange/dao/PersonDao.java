package com.yeremy.giftexchange.dao;

import com.yeremy.giftexchange.domain.Person;

import java.util.List;

public interface PersonDao
{
    List<Person> getPersons(String familyGroup);
}
