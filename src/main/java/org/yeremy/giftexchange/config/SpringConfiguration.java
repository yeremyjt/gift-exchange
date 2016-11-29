package org.yeremy.giftexchange.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@PropertySource("classpath:${spring.profiles.active}.properties")
public class SpringConfiguration
{
    @Value("${spring.profiles.active}")
    private String environment;

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo()
    {
        final ApiInfo apiInfo = new ApiInfo(
                "GiftExchange Service REST API",
                "Yeremy's Gift Exchange Service.",
                "1",
                "yeremyturcios@gmai.com",
                "API License",
                "API License URL", null);
        return apiInfo;
    }

    @Bean
    public DataSource dataSource(@Value("${mysql.username}") String mysqlUsername,
            @Value("${mysql.password}") String mysqlPassword,
            @Value("${mysql.url}") String mysqlUrl)
    {

        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(mysqlUsername);
        dataSource.setPassword(mysqlPassword);
        dataSource.setJdbcUrl(mysqlUrl);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource)
    {
        final JdbcTemplate template = new JdbcTemplate(dataSource);
        template.setQueryTimeout(15);
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        return namedParameterJdbcTemplate;
    }
}
