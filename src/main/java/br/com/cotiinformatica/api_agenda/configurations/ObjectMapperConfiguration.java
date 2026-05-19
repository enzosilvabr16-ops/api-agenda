package br.com.cotiinformatica.api_agenda.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();


    }

}
