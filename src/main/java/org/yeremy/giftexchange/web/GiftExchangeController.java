package org.yeremy.giftexchange.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yeremy.giftexchange.domain.GiftExchangeService;
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

    public List<GiftSet> getGiftExchangeList(@PathVariable String familyGroup)
    {
        return giftExchangeService.getGiftExchangeList(familyGroup);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(GiftExchangeController.class, args);
    }
}
