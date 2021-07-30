package com.epam.esm.registration;

import com.epam.esm.dto.UserDto;
import com.epam.esm.propReader.PropertiesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RegistrationClient {
    private final WebClient webClient;
    private final PropertiesReader propertiesReader;
    private String body = """
                {"firstName":"%s","lastName":"%s", 
                "enabled":"true", "username":"%s", 
                "credentials":[{"type":"password","value":"%s","temporary":false}]}}
                """;
    @Autowired
    public RegistrationClient(PropertiesReader propertiesReader) {
        this.propertiesReader = propertiesReader;
        this.webClient = WebClient.builder().baseUrl(propertiesReader.readProperty("baseUrl")).build();
    }

    public String obtainAccessToken(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", propertiesReader.readProperty("client_id"));
        formData.add("client_secret", propertiesReader.readProperty("client_secret"));
        formData.add("grant_type", propertiesReader.readProperty("grant_type"));
        String response = webClient
                .post()
                .uri(propertiesReader.readProperty("accessTokenUri"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    return response.split(",")[0].substring(17).replaceAll("\"", "");
    }

    public String registerUser(UserDto user){
        body = String.format(body, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
        String accessToken = obtainAccessToken();
          WebClient
                 .builder()
                 .baseUrl(propertiesReader.readProperty("baseUrl"))
                 .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                 .build()
                 .post()
                 .uri(propertiesReader.readProperty("registerUserUrl"))
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(BodyInserters.fromValue(body))
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
          return null;
    }
}
