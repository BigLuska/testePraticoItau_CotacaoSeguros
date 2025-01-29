package br.com.seguros.cotacao.application.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void testRestTemplateBean() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        Mockito.when(applicationContext.getBean(RestTemplate.class)).thenReturn(restTemplate);

        assertNotNull(applicationContext.getBean(RestTemplate.class), "RestTemplate deve ser criado");
    }
}
