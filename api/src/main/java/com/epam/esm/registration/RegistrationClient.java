package com.epam.esm.registration;

import com.epam.esm.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Component
public class RegistrationClient {
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8180/";
    private final String accessTokenUri = "auth/realms/master/protocol/openid-connect/token";
    private final String registerUserUri = "auth/admin/realms/SpringBootKeycloak/users";

    public RegistrationClient() {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public String obtainAccessToken(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "admin-cli");
        formData.add("client_secret", "4c6f2cfa-8123-4bb3-a488-05518c997cac");
        formData.add("grant_type", "client_credentials");
        String response = webClient
                .post()
                .uri(accessTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
    return response.split(",")[0].substring(17).replaceAll("\"", "");
    }

    public String registerUser(UserDto user){
        String body = """
                {"firstName":"%s","lastName":"%s", 
                "enabled":"true", "username":"%s", 
                "credentials":[{"type":"password","value":"%s","temporary":false}]}}
                """;
        body = String.format(body, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
        String accessToken = obtainAccessToken();
          WebClient
                 .builder()
                 .baseUrl(baseUrl)
                 .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                 .build()
                 .post()
                 .uri(registerUserUri)
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(BodyInserters.fromValue(body))
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
          return null;
    }
}
