package com.chatbot.services;

import com.chatbot.dtos.ChatRequestDto;
import com.chatbot.dtos.MessageDto;
import com.chatbot.entities.Conversation;
import com.chatbot.entities.Message;
import com.chatbot.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final GroqService groqService;
    
    @Transactional
    public String processChat(ChatRequestDto chatRequestDTO) {
        Conversation conversation;
        if (chatRequestDTO.getConversationId() != null) {
            conversation = conversationService.getConversationById(chatRequestDTO.getConversationId());
        } else {
            String title = generateConversationTitle(chatRequestDTO.getMessage());
            conversation = conversationService.createConversation(chatRequestDTO.getUserId(), title);
        }
        
        List<MessageDto> historyDTO = buildConversationHistory(conversation.getId());
        
        // formattazione
        String messageWithInstruction = chatRequestDTO.getMessage() + ".Fornisci un output senza markdown/abbellimenti stilistici del testo. Usa solo punteggiatura comune nelle tue risposte";

        historyDTO.add(new MessageDto("user", messageWithInstruction));
        
        // risposta servizio groq
        String aiResponse = groqService.chat(historyDTO);
        
        Message userMessage = new Message("user", chatRequestDTO.getMessage());
        conversation.addMessage(userMessage);
        messageRepository.save(userMessage);

        Message assistantMessage = new Message("assistant", aiResponse);
        conversation.addMessage(assistantMessage);
        messageRepository.save(assistantMessage);
        
        return aiResponse;
    }
    
    private List<MessageDto> buildConversationHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        List<MessageDto> history = new ArrayList<>();
        
        for (Message message : messages) {
            history.add(new MessageDto(message.getRole(), message.getContent()));
        }
        
        return history;
    }
    
    public List<Message> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
    
    private String generateConversationTitle(String firstMessage) {
        String title = firstMessage.split("\n")[0];
        return title.length() > 50 ? title.substring(0, 47) + "..." : title;
    }
}