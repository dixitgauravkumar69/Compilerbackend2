package com.example.POD.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GetEmbeding {

    private final RestTemplate restTemplate = new RestTemplate();

    public float[] callPythonAPI(String text) {

        String url = "https://embedding-generation-2.onrender.com/embedding";

        Map<String, String> body = new HashMap<>();
        body.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        // API response as object
        Map<String, Object> response =
                restTemplate.postForObject(url, request, Map.class);

        // extract embedding array
        List<Number> values =
                (List<Number>) response.get("embedding");

        float[] vector = new float[values.size()];

        for (int i = 0; i < values.size(); i++) {
            vector[i] = values.get(i).floatValue();
        }

        return vector;
    }
}