package com.orbit.notificationservice.consumer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbit.notificationservice.dto.NotificationRequestDto;
import com.orbit.notificationservice.service.NotificationService;

@Service
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<String> processedEvents = new HashSet<>();

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void consumePaymentEvent(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            String eventId = root.has("paymentId")
                    ? "PAY-" + root.get("paymentId").asText()
                    : "ORDER-" + root.get("orderId").asText();

            if (processedEvents.contains(eventId)) {
                System.out.println("Duplicate event detected (" + eventId + "). Skipping notification creation.");
                return;
            }

            processedEvents.add(eventId);

            String eventType = root.get("eventType").asText();
            String userId = root.has("userId") ? root.get("userId").asText() : "unknown";
            Long orderId = root.get("orderId").asLong();
            Double amount = root.get("amount").asDouble();

            StringBuilder details = new StringBuilder();
            if ("PAYMENT_REFUNDED".equals(eventType)) {
                details.append("Payment refund of ₹").append(amount).append(" processed for Order #").append(orderId).append(". Items: ");
            } else {
                details.append("Payment of ₹").append(amount).append(" received for Order #").append(orderId).append(". Items: ");
            }

            JsonNode itemsNode = root.get("items");
            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    String name = item.has("productName") ? item.get("productName").asText() : ("Product #" + item.get("productId").asText());
                    int qty = item.get("quantity").asInt();
                    details.append("[").append(name).append(" x ").append(qty).append("] ");
                }
            }

            NotificationRequestDto dto = new NotificationRequestDto();
            dto.setUserId(userId);
            dto.setType(eventType);
            dto.setMessage(details.toString());

            notificationService.createNotification(dto);
            System.out.println("Notification saved for User " + userId + " (Event ID: " + eventId + "): " + details);

        } catch (Exception e) {
            System.err.println("Error processing notification Kafka event: " + e.getMessage());
        }
    }
}