package com.xingcdev.museum.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Getter
public class AuthUtils {

    private final String authorizationServerUri = "http://localhost:9000/realms/museum-dev";

    public AuthInfo authenticate() {
        URI uri = UriComponentsBuilder
                .fromUriString(authorizationServerUri + "/protocol/openid-connect/token")
                .build()
                .toUri();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("museum-app"));
        formData.put("username", Collections.singletonList("test"));
        formData.put("password", Collections.singletonList("test"));
        formData.put("scope", Collections.singletonList("openid"));

        WebClient webClient = WebClient.builder().build();
        String result = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        var accessToken = jsonParser.parseMap(result)
                .get("access_token")
                .toString();

        var jwtDecoder = JwtDecoders.fromIssuerLocation(authorizationServerUri);
        var jwt = jwtDecoder.decode(accessToken);

        return new AuthInfo(accessToken, jwt.getSubject());
    }

    @Getter
    @AllArgsConstructor
    public class AuthInfo {
        private String accessToken;
        private String userId;
    }
}
