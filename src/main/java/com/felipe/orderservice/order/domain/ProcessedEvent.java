package com.felipe.orderservice.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Table(name = "processed_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessedEvent {

    @Id
    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "consumer_name", nullable = false)
    private String consumerName;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    private ProcessedEvent(UUID eventId, String eventType, String consumerName, Instant processedAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.consumerName = consumerName;
        this.processedAt = processedAt;
    }

    public static ProcessedEvent create(UUID eventId, String eventType, String consumerName) {
        validateEventId(eventId);
        validateEventType(eventType);
        validateConsumerName(consumerName);

        return new ProcessedEvent(
                eventId,
                eventType,
                consumerName,
                Instant.now()
        );
    }

    private static void validateEventId(UUID eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event id cannot be null");
        }
    }

    private static void validateEventType(String eventType) {
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("Event type cannot be null or blank");
        }

        if (eventType.length() > 120) {
            throw new IllegalArgumentException("Event type cannot have more than 120 characters");
        }
    }

    private static void validateConsumerName(String consumerName) {
        if (consumerName == null || consumerName.isBlank()) {
            throw new IllegalArgumentException("Consumer name cannot be null or blank");
        }

        if (consumerName.length() > 120) {
            throw new IllegalArgumentException("Consumer name cannot have more than 120 characters");
        }
    }
}

/* consumer.isBlank verifica se o nome é valido

    Exemplos de não válidos
    ""
    " "
    "     "
    "\n"
    "\t"

    Exemplos de válidos
    "Felipe Souza Moreira"
    "stock-reserved-consumer"
 */