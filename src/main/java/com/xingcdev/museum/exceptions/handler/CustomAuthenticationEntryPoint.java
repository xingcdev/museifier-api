package com.xingcdev.museum.exceptions.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingcdev.museum.domain.dto.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var objectMapper = new ObjectMapper();

        var errorDTO = ErrorDto.builder()
                .code("unauthenticated")
                .message("No token is found. Please try again.")
                .build();
        var dtoString = objectMapper.writeValueAsString(errorDTO);

        // Customize the response body
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(dtoString);
    }
}
