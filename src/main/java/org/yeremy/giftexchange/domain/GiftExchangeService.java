package org.yeremy.giftexchange.domain;

import java.util.List;

import org.yeremy.giftexchange.dto.GiftSet;

public interface GiftExchangeService
{
    List<GiftSet> getGiftExchangeList(String familyGroup);
}
