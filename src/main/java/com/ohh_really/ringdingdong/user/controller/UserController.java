package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.AuthorizationCode;
import com.ohh_really.ringdingdong.user.service.GoogleOAuth2Service;
import com.ohh_really.ringdingdong.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Autowired
    public UserController(GoogleOAuth2Service googleOAuth2Service, UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @GetMapping("/google/login")
    @Operation(summary = "Google Login 주소")
    public String googleLogin() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth";
        url += "?response_type=code";
        url += "&client_id=" + clientId;
        url += "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
        url += "&redirect_uri=" + redirectUri;
        return url;
    }


    @GetMapping("/google/redirect")
    public ResponseEntity<AuthorizationCode> googleRedirect(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope,
            @RequestParam("authuser") String authuser,
            @RequestParam("prompt") String prompt
    ) {
        return googleOAuth2Service.startWithGoogle(code);
    }

}
