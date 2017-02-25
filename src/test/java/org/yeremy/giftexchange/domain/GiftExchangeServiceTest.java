package org.yeremy.giftexchange.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.yeremy.giftexchange.dao.ExchangeHistoryDao;
import org.yeremy.giftexchange.dao.PersonDao;
import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;

@RunWith(MockitoJUnitRunner.class)
public class GiftExchangeServiceTest
{
    @Mock
    private PersonDao personDao;

    @Mock
    private ExchangeHistoryDao exchangeHistoryDao;

    @InjectMocks
    private GiftExchangeService giftExchangeService = new GiftExchangeServiceImpl();

    private List<ExchangeHistory> thisYearExchangeHistory;
    private List<ExchangeHistory> oneYearsAgoExchangeHistory;
    private List<ExchangeHistory> twoYearsAgoExchangeHistory;
    private List<ExchangeHistory> threeYearsAgoExchangeHistory;
    private List<ExchangeHistory> fourYearsAgoExchangeHistory;
    private List<ExchangeHistory> fiveYearsAgoExchangeHistory;

    @Before
    public void init()
    {
        thisYearExchangeHistory = new ArrayList<>();

        oneYearsAgoExchangeHistory = new ArrayList<>();
        ExchangeHistory oneYearAgo_1 = new ExchangeHistory();
        oneYearAgo_1.setId(1);
        oneYearAgo_1.setGiverId(1);
        oneYearAgo_1.setGiverName("Ben Pixton");
        oneYearAgo_1.setReceiverId(10);
        oneYearAgo_1.setReceiverName("Yeremy Turcios");
        oneYearAgo_1.setFamilyGroup("PIXTON");
        oneYearAgo_1.setYear(2015);

        ExchangeHistory oneYearAgo_2 = new ExchangeHistory();
        oneYearAgo_2.setId(2);
        oneYearAgo_2.setGiverId(2);
        oneYearAgo_2.setGiverName("Erika Pixton");
        oneYearAgo_2.setReceiverId(11);
        oneYearAgo_2.setReceiverName("Allison Turcios");
        oneYearAgo_2.setFamilyGroup("PIXTON");
        oneYearAgo_2.setYear(2015);

        ExchangeHistory oneYearAgo_3 = new ExchangeHistory();
        oneYearAgo_3.setId(3);
        oneYearAgo_3.setGiverId(5);
        oneYearAgo_3.setGiverName("Sam Christensen");
        oneYearAgo_3.setReceiverId(1);
        oneYearAgo_3.setReceiverName("Ben Pixton");
        oneYearAgo_3.setFamilyGroup("PIXTON");
        oneYearAgo_3.setYear(2015);

        ExchangeHistory oneYearAgo_4 = new ExchangeHistory();
        oneYearAgo_4.setId(4);
        oneYearAgo_4.setGiverId(7);
        oneYearAgo_4.setGiverName("Megan Christensen");
        oneYearAgo_4.setReceiverId(3);
        oneYearAgo_4.setReceiverName("Erika Pixton");
        oneYearAgo_4.setFamilyGroup("PIXTON");
        oneYearAgo_4.setYear(2015);

        ExchangeHistory oneYearAgo_5 = new ExchangeHistory();
        oneYearAgo_5.setId(5);
        oneYearAgo_5.setGiverId(8);
        oneYearAgo_5.setGiverName("Kim Pixton");
        oneYearAgo_5.setReceiverId(5);
        oneYearAgo_5.setReceiverName("Sam Christensen");
        oneYearAgo_5.setFamilyGroup("PIXTON");
        oneYearAgo_5.setYear(2015);

        ExchangeHistory oneYearAgo_6 = new ExchangeHistory();
        oneYearAgo_6.setId(6);
        oneYearAgo_6.setGiverId(10);
        oneYearAgo_6.setGiverName("Yeremy Turcios");
        oneYearAgo_6.setReceiverId(6);
        oneYearAgo_6.setReceiverName("Mevaluegan Christensen");
        oneYearAgo_6.setFamilyGroup("PIXTON");
        oneYearAgo_6.setYear(2015);

        ExchangeHistory oneYearAgo_7 = new ExchangeHistory();
        oneYearAgo_7.setId(7);
        oneYearAgo_7.setGiverId(11);
        oneYearAgo_7.setGiverName("Allison Turcios");
        oneYearAgo_7.setReceiverId(8);
        oneYearAgo_7.setReceiverName("Kim Pixton");
        oneYearAgo_7.setFamilyGroup("PIXTON");
        oneYearAgo_7.setYear(2015);

        ExchangeHistory oneYearAgo_8 = new ExchangeHistory();
        oneYearAgo_8.setId(8);
        oneYearAgo_8.setGiverId(9);
        oneYearAgo_8.setGiverName("Maddison Sigmon");
        oneYearAgo_8.setReceiverId(3);
        oneYearAgo_8.setReceiverName("Carly Pixton");
        oneYearAgo_8.setFamilyGroup("PIXTON");
        oneYearAgo_8.setYear(2015);

        ExchangeHistory oneYearAgo_9 = new ExchangeHistory();
        oneYearAgo_9.setId(9);
        oneYearAgo_9.setGiverId(3);
        oneYearAgo_9.setGiverName("Carly Pixton");
        oneYearAgo_9.setReceiverId(9);
        oneYearAgo_9.setReceiverName("Maddison Sigmon");
        oneYearAgo_9.setFamilyGroup("PIXTON");
        oneYearAgo_9.setYear(2015);

        oneYearsAgoExchangeHistory.addAll(Arrays.asList(oneYearAgo_1, oneYearAgo_2, oneYearAgo_3, oneYearAgo_4,
                oneYearAgo_5, oneYearAgo_6, oneYearAgo_7, oneYearAgo_8, oneYearAgo_9));

        twoYearsAgoExchangeHistory = new ArrayList<>();
        threeYearsAgoExchangeHistory = new ArrayList<>();
        fourYearsAgoExchangeHistory = new ArrayList<>();
        fiveYearsAgoExchangeHistory = new ArrayList<>();
    }

    @Test
    public void testGetGiftExchangeList_Ok()
    {
        when(exchangeHistoryDao.getExchangeHistory(anyString(), anyInt()))
                .thenReturn(thisYearExchangeHistory)
                .thenReturn(oneYearsAgoExchangeHistory)
                .thenReturn(twoYearsAgoExchangeHistory)
                .thenReturn(threeYearsAgoExchangeHistory)
                .thenReturn(fourYearsAgoExchangeHistory)
                .thenReturn(fiveYearsAgoExchangeHistory);

        List<GiftSet> result = giftExchangeService.getGiftExchangeList("Pixton", false);
        assertNotNull(result);
    }

}
