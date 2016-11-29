package org.yeremy.giftexchange.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.yeremy.giftexchange.domain.GiftExchangeService;
import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RestController
@ComponentScan("org.yeremy.giftexchange")
@RequestMapping("/giftExchange")
@Api(value = "/giftExchange")
public class GiftExchangeController
{
    @Inject
    private GiftExchangeService giftExchangeService;

    @RequestMapping(path = "/{familyGroup}", method = RequestMethod.POST, produces = "application/json", consumes = "text/plain")
    public List<GiftSet> getGiftExchangeList(@PathVariable String familyGroup, @RequestParam(value = "record", required = false) Boolean record)
    {
        return giftExchangeService.getGiftExchangeList(familyGroup, record);
    }

    @RequestMapping(path = "/{familyGroup}/{year}", method = RequestMethod.GET, produces = "application/json")
    public List<ExchangeHistory> getExchangeHistory(@PathVariable("familyGroup") String familyGroup, @PathVariable("year") int year)
    {
        return giftExchangeService.getExchangeHistory(familyGroup, year);
    }
}
