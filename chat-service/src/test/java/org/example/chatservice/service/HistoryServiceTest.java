package org.example.chatservice.service;

import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHistoryByUserId() {
        String userId = "1";
        List<ChatMessage> expectedMessages = Arrays.asList(
                new ChatMessage(userId, "Hello", "Hi!", LocalDateTime.now())
        );

        when(chatMessageRepository.findByUserId(userId)).thenReturn(expectedMessages);

        List<ChatMessage> result = historyService.getHistoryByUserId(userId);

        assertEquals(expectedMessages, result);
        verify(chatMessageRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetAllMessages() {
        List<ChatMessage> expectedMessages = Arrays.asList(
                new ChatMessage("1", "Hello", "Hi!", LocalDateTime.now()),
                new ChatMessage("2", "How are you?", "I'm fine, thanks!", LocalDateTime.now())
        );

        when(chatMessageRepository.findAll()).thenReturn(expectedMessages);

        List<ChatMessage> result = historyService.getAllMessages();

        assertEquals(expectedMessages, result);
        verify(chatMessageRepository, times(1)).findAll();
    }

    @Test
    public void testSaveMessage() {
        ChatMessage message = new ChatMessage("1", "Hello", "Hi!", LocalDateTime.now());

        when(chatMessageRepository.save(message)).thenReturn(message);

        ChatMessage result = historyService.saveMessage(message);

        assertEquals(message, result);
        verify(chatMessageRepository, times(1)).save(message);
    }
}
