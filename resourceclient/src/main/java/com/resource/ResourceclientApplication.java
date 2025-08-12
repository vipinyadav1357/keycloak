package com.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ResourceclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceclientApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// it will keep track of authorize client and save access token in memory
	@Bean
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService(
			ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean
	OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
		var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
				oAuth2AuthorizedClientService);
		OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials()
				.build();
		manager.setAuthorizedClientProvider(provider);
		return manager;
	}

	@Bean
	CommandLineRunner runner(OAuth2AuthorizedClientManager manager, RestTemplate template,
			@Value("${service2.url}") String service2url) {
		return args -> {
			var authReq = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak-client").principal("machine")
					.build();
			var client = manager.authorize(authReq);
			var token = client.getAccessToken().getTokenValue();
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			var response = template.exchange(service2url + "/data", HttpMethod.GET, new HttpEntity<>(headers),
					String.class);
			System.out.print(response);
		};
	}

}
