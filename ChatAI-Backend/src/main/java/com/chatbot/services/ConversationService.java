package com.chatbot.services;

import com.chatbot.dtos.ConversationDto;
import com.chatbot.entities.Conversation;
import com.chatbot.entities.User;
import com.chatbot.repositories.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    
    @Transactional
    public Conversation createConversation(Integer userId, String title) {
        User user = userService.getUserById(userId);
        
        Conversation conversation = new Conversation();
        conversation.setUser(user);
        conversation.setTitle(title != null ? title : "New Conversation");
        
        return conversationRepository.save(conversation);
    }
    
    public Conversation getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation with id " + conversationId + " not found"));
    }
    
    public List<ConversationDto> getUserConversations(Integer userId) {
        List<Conversation> conversations = conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        
        return conversations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }
    
    @Transactional
    public Conversation updateConversationTitle(Long conversationId, String newTitle) {
        Conversation conversation = getConversationById(conversationId);
        conversation.setTitle(newTitle);
        return conversationRepository.save(conversation);
    }
    
    private ConversationDto toDTO(Conversation conversation) {
        return new ConversationDto(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                conversation.getMessages().size()
        );
    }
}