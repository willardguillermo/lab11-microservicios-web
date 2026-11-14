package com.tecsup.config;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.net.http.HttpClient;
import java.time.Duration;

// Configuración común para las llamadas HTTP a otros microservicios
@Configuration
public class RestClientConfig {

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        // HTTP/1.1: evita que el cliente del JDK intente subir a HTTP/2 (h2c) y se quede esperando.
        // Timeouts: si otro servicio no responde, fallamos rápido en vez de colgar la petición.
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(5));
        return builder -> builder.requestFactory(factory);
    }
}
