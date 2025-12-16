package com.chatbot.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String data;
    private String timestamp;
    private String message;
    
    public ApiResponse(boolean success, String data) {
        this.success = success;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }
}