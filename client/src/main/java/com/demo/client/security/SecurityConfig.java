package com.demo.client.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
				req->req.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()))
				.build();
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

}
