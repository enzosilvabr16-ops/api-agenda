package br.com.cotiinformatica.api_agenda.configurations;

import br.com.cotiinformatica.api_agenda.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Value("${jwt.secret}")
    private String secretyKey;

    @Bean
    FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {

        //registrando a classe Filter que iremos configurar
        FilterRegistrationBean<AuthenticationFilter>
                registration = new FilterRegistrationBean<>();

        //Configurando o filter
        registration.setFilter(new AuthenticationFilter(secretyKey));

        //Definindo os endpoints que o filter irá proteger
        registration.addUrlPatterns("/api/usuario/obter-dados");

        return registration;

    }
}

