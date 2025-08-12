package com.demo.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ServiceToClient {
    private final RestTemplate template;
    private final OAuth2AuthorizedClientManager manager;

    @Value("${service2.url}")
    String service2url;
    public String fetchData() {
        var authReq = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak-client").principal("machine")
                .build();
        var client = manager.authorize(authReq);
        assert client != null;
        var token = client.getAccessToken().getTokenValue();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var response = template.exchange(service2url + "/data", HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
        return response.getBody();
    }
}
