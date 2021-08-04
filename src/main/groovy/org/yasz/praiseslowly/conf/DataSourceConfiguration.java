package org.yasz.praiseslowly.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name="customDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource customDataSource() {
        return DataSourceBuilder.create().build();
    }
}