package org.yeremy.giftexchange.IT;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yeremy.giftexchange.config.TestConfig;
import org.yeremy.giftexchange.dao.PersonDao;
import org.yeremy.giftexchange.domain.GiftExchangeService;
import org.yeremy.giftexchange.domain.Person;
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

    @Inject
    private PersonDao personDao;

    @Test
    public void testService_Ok()
    {
        List<GiftSet> giftSets = giftExchangeService.getGiftExchangeList("PIXTON");
        assertNotNull(giftSets);
    }

    @Test
    public void loadTest()
    {
        List<Person> persons = personDao.getPersons("PIXTON");

        for (int i = 0; i < 1000; i++)
        {
            List<GiftSet> giftSets = giftExchangeService.getGiftExchangeList("PIXTON");
            assertNotNull(giftSets);

            for (Person person : persons)
            {
                boolean found = false;
                for (GiftSet giftSet : giftSets)
                {
                    if (giftSet.getGiver().equals(person))
                    {
                        found = true;
                        break;
                    }
                }

                assertTrue(found);

                found = false;
                for (GiftSet giftSet : giftSets)
                {
                    if (giftSet.getReceiver().equals(person))
                    {
                        found = true;
                        break;
                    }
                }

                assertTrue(found);
            }
        }
    }
}
