package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public ResponseEntity<String> home(OAuth2AuthenticationToken token){
		return ResponseEntity.ok("Hello "+token.getPrincipal().getAttribute("email"));
	}

}