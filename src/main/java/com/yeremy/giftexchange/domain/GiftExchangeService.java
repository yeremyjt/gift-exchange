package com.yeremy.giftexchange.domain;

import com.yeremy.giftexchange.dto.GiftSet;

import java.util.List;

public interface GiftExchangeService
{
    List<GiftSet> getGiftExchangeList(String familyGroup);
}
