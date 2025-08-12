package com.demo.client.Controller;


import com.demo.client.service.ServiceToClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
@CrossOrigin
public class ProxyController {
    private final ServiceToClient client;

    @GetMapping("/hello")
    public ResponseEntity<String> proxy(){
        return ResponseEntity.ok(client.fetchData());
    }
}
