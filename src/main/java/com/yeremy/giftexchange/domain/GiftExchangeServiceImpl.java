package com.yeremy.giftexchange.domain;

import com.yeremy.giftexchange.dao.PersonDao;
import com.yeremy.giftexchange.dto.GiftSet;

import javax.inject.Inject;
import java.util.List;

public class GiftExchangeServiceImpl implements GiftExchangeService
{
    @Inject
    PersonDao personDao;

    @Override
    public List<GiftSet> getGiftExchangeList(String familyGroup)
    {
        List<Person> persons = personDao.getPersons(familyGroup);

        return null;
    }
}
