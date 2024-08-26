package org.example.subscriptionservice.service;

import org.example.subscriptionservice.model.PaymentStatus;
import org.example.subscriptionservice.model.Subscription;
import org.example.subscriptionservice.model.SubscriptionType;
import org.example.subscriptionservice.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleSubscription() {
        // Arrange
        String message = "1:ONE_MONTH:token123";
        SubscriptionType expectedSubscriptionType = SubscriptionType.ONE_MONTH;
        PaymentStatus expectedPaymentStatus = PaymentStatus.SUCCESSFUL;

        // Act
        subscriptionService.handleSubscription(message);

        // Assert
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));

        // Capture the subscription object passed to save method
        verify(subscriptionRepository).save(argThat(subscription ->
                subscription.getUserId().equals("1") &&
                        subscription.getSubscriptionType().equals(expectedSubscriptionType) &&
                        subscription.getPaymentStatus().equals(expectedPaymentStatus)
        ));
    }
}
