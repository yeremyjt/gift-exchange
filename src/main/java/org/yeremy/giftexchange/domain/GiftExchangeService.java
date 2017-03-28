package org.yeremy.giftexchange.domain;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;

public interface GiftExchangeService
{
    List<GiftSet> getGiftExchangeList(String familyGroup, Boolean record)
            throws FileNotFoundException, UnsupportedEncodingException;

    List<ExchangeHistory> getExchangeHistory(String familyGroup, int year);
}
