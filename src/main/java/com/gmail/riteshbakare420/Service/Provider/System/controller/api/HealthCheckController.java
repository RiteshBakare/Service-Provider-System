package com.gmail.riteshbakare420.Service.Provider.System.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class HealthCheckController {

    @GetMapping("/")
    public String healthCheck() {
        return "Hello World";
    }

}