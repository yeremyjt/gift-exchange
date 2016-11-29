package org.yeremy.giftexchange.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yeremy.giftexchange.dto.ExchangeHistory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Singleton
public class ExchangeHistoryDaoImpl implements ExchangeHistoryDao
{
    @Inject
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<ExchangeHistory> getExchangeHistory(String familyGroup, int year)
    {
        String sql = "SELECT * FROM exchange_history WHERE family_group=:family_group AND year=:year";
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("family_group", familyGroup);

        try
        {
            return jdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(ExchangeHistory.class));
        }
        catch (EmptyResultDataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public int writeExchangeHistory(ExchangeHistory exchangeHistory)
    {
        String sql = "INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) "
                + "VALUES (:giver_id, :giver_name, :receiver_id, :receiver_name, :family_group, :year)";
        Map<String, Object> params = new HashMap<>();
        params.put("giver_id", exchangeHistory.getGiverId());
        params.put("giver_name", exchangeHistory.getGiverName());
        params.put("receiver_id", exchangeHistory.getReceiverId());
        params.put("receiver_name", exchangeHistory.getReceiverName());
        params.put("family_group", exchangeHistory.getFamilyGroup());
        params.put("year", exchangeHistory.getYear());

        final SqlParameterSource paramSource = new MapSqlParameterSource(params);
        final KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, paramSource, generatedKeyHolder);
        return generatedKeyHolder.getKey().intValue();
    }
}
