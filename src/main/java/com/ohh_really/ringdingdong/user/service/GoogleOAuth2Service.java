package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.user.UserRole;
import com.ohh_really.ringdingdong.user.dto.AuthorizationCode;
import com.ohh_really.ringdingdong.user.dto.GoogleUserInfo;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import com.ohh_really.ringdingdong.user.entity.User;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Set;


@Service
public class GoogleOAuth2Service {


    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Autowired
    public GoogleOAuth2Service(UserRepository userRepository, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }


    public ResponseEntity<AuthorizationCode> startWithGoogle(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());

        AuthorizationCode authorizationCode = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token",
                request,
                AuthorizationCode.class, params
        );
        GoogleUserInfo googleUserInfo = getInfo(authorizationCode.getAccess_token());

        System.out.println(googleUserInfo.toString());
        if (!userRepository.existsByEmail(googleUserInfo.getEmail())) {
            this.registerUser(googleUserInfo);
        }
        return ResponseEntity.ok(authorizationCode);
    }


    public GoogleUserInfo getInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);

        return restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                request,
                GoogleUserInfo.class
        ).getBody();
    }

    public UserInfoDto registerUser(GoogleUserInfo googleUserInfo) {
        User user = User.builder()
                .email(googleUserInfo.getEmail())
                .username(googleUserInfo.getName())
                .enabled(true)
                .accountNonExpired(false)
                .accountNonLocked(false)
                .roles(Set.of(UserRole.USER))
                .build();
        return modelMapper.map(userRepository.save(user), UserInfoDto.class);

    }
}
