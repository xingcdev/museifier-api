package com.xingcdev.museum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${museifier.oauth2.issuer-uri}")
    String authorizationServerUrl;

    @Value("${museifier.oauth2.token-uri}")
    String authorizationTokenUrl;

    private static final String OAUTH_SCHEME_NAME = "OAuth_security_schema";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                .info(new Info().title("Museum App (Dev)")
                        .description("Managing your visited museums.")
                        .version("1.0"));
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow flow = createAuthorizationCodeFlow();
        return new OAuthFlows().authorizationCode(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        return new OAuthFlow()
                .authorizationUrl(authorizationServerUrl + "/protocol/openid-connect/auth")
                .tokenUrl(authorizationTokenUrl)
                .refreshUrl(authorizationTokenUrl)
                .scopes(new Scopes().addString("openid", "default scope"));
    }
}
