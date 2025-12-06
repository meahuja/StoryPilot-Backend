package com.samsung.storypilot.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String openAiModel;

    @Value("${openai.temperature}")
    private int openAiTemperature;

    @Value("${openai.prompt}")
    private String openAiPrompt;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateJsonFromText(String extractedText, String jsonPrompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        String prompt = openAiPrompt.replace("{content}", extractedText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> message1 = new HashMap<>();
        message1.put("role", "system");
        message1.put("content", "");

        Map<String, Object> message2 = new HashMap<>();
        message2.put("role", "user");
        message2.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", openAiModel);
        body.put("messages", new Object[]{message1, message2});
        body.put("temperature", openAiTemperature);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String,Object> responseBody = response.getBody();
            var choices = (java.util.List<Map<String,Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String,Object> choice = choices.get(0);
                Map<String,Object> message = (Map<String,Object>) choice.get("message");
                return (String) message.get("content");
            }
        }
        return null;
    }
}
