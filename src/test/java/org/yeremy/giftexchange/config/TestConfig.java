package org.yeremy.giftexchange.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource("classpath:${spring.profiles.active}.properties")
@ComponentScan(basePackages = { "org.yeremy.giftexchange.dao", "org.yeremy.giftexchange.domain",
        "org.yeremy.giftexchange.dto" })
public class TestConfig
{
    @Bean
    public EmbeddedDatabase dataSource() throws SQLException
    {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        final EmbeddedDatabase dataSource = builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:giftexchange_db_create.sql")
                .addScript("classpath:giftexchange_db_insert_test_data.sql")
                .build();

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
