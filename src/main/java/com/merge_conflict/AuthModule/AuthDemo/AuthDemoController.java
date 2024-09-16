package com.merge_conflict.AuthModule.AuthDemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthDemoController {

    @PostMapping(value = "demo")
    public String welcome()
    {
        return "Welcome!";
    }
}