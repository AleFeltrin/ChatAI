package com.chatbot.controllers;

import com.chatbot.dtos.ConversationDto;
import com.chatbot.entities.Conversation;
import com.chatbot.services.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "http://localhost:4200")
public class ConversationController {
    
    private final ConversationService conversationService;
    
    @GetMapping("/user/{userId}")
    public List<ConversationDto> getUserConversations(@PathVariable Integer userId) {
        return conversationService.getUserConversations(userId);
    }
    
    @PostMapping
    public Conversation createConversation(@RequestBody Map<String, Object> request) {
        Integer userId = (Integer) request.get("userId");
        String title = (String) request.get("title");
        return conversationService.createConversation(userId, title);
    }
    
    @PutMapping("/{conversationId}/title")
    public Conversation updateTitle(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> request) {
        return conversationService.updateConversationTitle(conversationId, request.get("title"));
    }
    
    @DeleteMapping("/{conversationId}")
    public void deleteConversation(@PathVariable Long conversationId) {
        conversationService.deleteConversation(conversationId);
    }
    
    @GetMapping("/{conversationId}")
    public Conversation getConversation(@PathVariable Long conversationId) {
        return conversationService.getConversationById(conversationId);
    }
}