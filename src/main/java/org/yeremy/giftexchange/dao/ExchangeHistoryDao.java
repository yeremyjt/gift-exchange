package org.yeremy.giftexchange.dao;

import org.yeremy.giftexchange.dto.ExchangeHistory;

import java.util.List;

public interface ExchangeHistoryDao
{
    List<ExchangeHistory> getExchangeHistory(String familyGroup, int year);
    int writeExchangeHistory(ExchangeHistory exchangeHistory);
}
