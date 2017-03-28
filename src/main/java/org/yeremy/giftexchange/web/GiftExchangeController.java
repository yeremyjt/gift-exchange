package org.yeremy.giftexchange.web;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yeremy.giftexchange.domain.GiftExchangeService;
import org.yeremy.giftexchange.dto.ExchangeHistory;
import org.yeremy.giftexchange.dto.GiftSet;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@ComponentScan("org.yeremy.giftexchange")
@RequestMapping("/giftExchange")
@Api(value = "/giftExchange")
public class GiftExchangeController
{
    @Inject
    private GiftExchangeService giftExchangeService;

    @RequestMapping(path = "/{familyGroup}", method = RequestMethod.GET, produces = "application/json")
    public List<GiftSet> getGiftExchangeList(@PathVariable("familyGroup") String familyGroup,
            @RequestParam(value = "record") Boolean record) throws FileNotFoundException, UnsupportedEncodingException
    {
        return giftExchangeService.getGiftExchangeList(familyGroup, record);
    }

    @RequestMapping(path = "/{familyGroup}/{year}", method = RequestMethod.GET, produces = "application/json")
    public List<ExchangeHistory> getExchangeHistory(@PathVariable("familyGroup") String familyGroup,
            @PathVariable("year") int year)
    {
        return giftExchangeService.getExchangeHistory(familyGroup, year);
    }
}
