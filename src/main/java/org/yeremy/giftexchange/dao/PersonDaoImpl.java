package org.yeremy.giftexchange.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.yeremy.giftexchange.domain.Person;

@Named
@Singleton
public class PersonDaoImpl implements PersonDao
{
    @Inject
    NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Person> getPersons(String familyGroup)
    {
        String sql = " SELECT  p.id, p.name, p.type, p.family_name, f.family_group_name "
                + "FROM  person p  LEFT JOIN family f ON (p.family_name = f.name) "
                + "WHERE f.family_group_name=:family_group_name";
        Map<String, Object> params = new HashMap<>();
        params.put("family_group_name", familyGroup);

        try
        {
            return jdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(Person.class));
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new NotFoundException(e);
        }
    }
}
