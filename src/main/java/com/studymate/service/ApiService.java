package com.studymate.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiService() {

        client = HttpClient.newHttpClient();

        mapper = new ObjectMapper();
    }

    public String getStudyQuote() throws Exception {

        String url =
                "https://zenquotes.io/api/random";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "API Error: " + response.statusCode()
            );
        }

        JsonNode root =
                mapper.readTree(response.body());

        return root.get(0)
                .get("q")
                .asText();
    }
}
