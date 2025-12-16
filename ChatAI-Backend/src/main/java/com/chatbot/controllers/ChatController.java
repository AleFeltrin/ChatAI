package com.chatbot.controllers;

import com.chatbot.dtos.ChatRequestDto;
import com.chatbot.responses.ApiResponse;
import com.chatbot.entities.Message;
import com.chatbot.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping
    public ApiResponse chat(@RequestBody ChatRequestDto chatRequestDTO) {
        try {
            String aiResponse = chatService.processChat(chatRequestDTO);
            return new ApiResponse(true, aiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, "Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/conversation/{conversationId}/messages")
    public List<Message> getConversationMessages(@PathVariable Long conversationId) {
        return chatService.getConversationMessages(conversationId);
    }
}