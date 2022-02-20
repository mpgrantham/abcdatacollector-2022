package com.canalbrewing.abcdatacollector.config;

import com.canalbrewing.abcdatacollector.resultsetmapper.ResultSetMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ResultSetMapper resultSetMapper() {
        return new ResultSetMapper();
    }

}
