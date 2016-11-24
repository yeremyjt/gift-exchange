package org.yeremy.giftexchange.IT;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yeremy.giftexchange.config.TestConfig;
import org.yeremy.giftexchange.domain.GiftExchangeService;
import org.yeremy.giftexchange.dto.GiftSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@DirtiesContext
public class GiftExchangeServiceTest
{
    @BeforeClass
    public static void init()
    {
        System.setProperty("spring.profiles.active", "loc");
    }

    @Inject
    private GiftExchangeService giftExchangeService;

    @Test
    public void testService_Ok()
    {
        List<GiftSet> giftSets = giftExchangeService.getGiftExchangeList("PIXTON");

        assertNotNull(giftSets);
    }
}
