package com.resource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/data")
	public ResponseEntity<String> home(  ){
		return ResponseEntity.ok("Hello ");
	}

}