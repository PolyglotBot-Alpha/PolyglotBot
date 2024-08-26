
package org.example.userservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.userservice.model.SubscriptionType;
import org.example.userservice.model.User;
import org.example.userservice.model.SubscriptionStatus;
import org.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User("1", "user1", "user1@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        User user2 = new User("2", "user2", "user2@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        String userId = "1";
        User user = new User(userId, "user1", "user1@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("user1", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        // Arrange
        String userId = "1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.getUserById(userId);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User("1", "user1", "user1@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser.getCreatedAt());
        assertEquals("user1", createdUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        String userId = "1";
        User existingUser = new User(userId, "user1", "user1@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        User updatedDetails = new User(userId, "updatedUser", "updatedUser@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedDetails);

        // Act
        User updatedUser = userService.updateUser(userId, updatedDetails);

        // Assert
        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("updatedUser@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Arrange
        String userId = "1";
        User updatedDetails = new User(userId, "updatedUser", "updatedUser@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(userId, updatedDetails));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateSubscriptionStatus() {
        User user = new User("1", "user1", "user1@example.com", null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), SubscriptionStatus.NONE, LocalDateTime.now(), 1L);

        when(userRepository.findByIdForUpdate("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.updateSubscriptionStatus("1", SubscriptionType.ONE_MONTH, "token123");

        // Check that the subscription status is updated correctly
        assertEquals(SubscriptionStatus.ACTIVE, user.getSubscriptionStatus());

        // Verify that the user is saved with the updated subscription status
        verify(userRepository, times(1)).save(user);

        // Construct the expected Kafka message
        String expectedMessage = "1:ONE_MONTH:token123";

        // Verify that the correct message is sent to Kafka
        verify(kafkaTemplate, times(1)).send("subscription_topic", expectedMessage);
    }


    @Test
    void testUpdateSubscriptionStatus_UserDoesNotExist() {
        // Arrange
        String userId = "1";
        SubscriptionType type = SubscriptionType.ONE_MONTH;
        String token = "someToken";

        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateSubscriptionStatus(userId, type, token));
        verify(userRepository, times(1)).findByIdForUpdate(userId);
    }

}
