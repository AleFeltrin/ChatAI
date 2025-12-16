package com.chatbot.services;

import com.chatbot.dtos.MessageDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {
    
    @Value("${groq.api.key}")
    private String apiKey;
    
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.1-8b-instant";
    
     private final RestTemplate restTemplate = new RestTemplate();
    
    public String chat(List<MessageDto> conversationHistory) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        
        List<Map<String, String>> messages = new ArrayList<>();
        for (MessageDto msg : conversationHistory) {
            if (msg != null && msg.getRole() != null && msg.getContent() != null) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("role", msg.getRole());
                messageMap.put("content", msg.getContent());
                messages.add(messageMap);
            } else {
                System.err.println("WARNING: Skipping null message or null fields");
            }
        }
        
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("No valid messages in conversation history");
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("messages", messages);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
        
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        
        return (String) message.get("content");
    }
}