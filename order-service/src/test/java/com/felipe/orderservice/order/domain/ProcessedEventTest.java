package com.felipe.orderservice.order.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessedEventTest {

    @Test
    void shouldCreateProcessedEvent() {
        UUID eventId = UUID.randomUUID();

        ProcessedEvent event = ProcessedEvent.create(eventId, "OrderCreated", "payment-consumer");

        assertThat(event.getEventId()).isEqualTo(eventId);
        assertThat(event.getEventType()).isEqualTo("OrderCreated");
        assertThat(event.getConsumerName()).isEqualTo("payment-consumer");
        assertThat(event.getProcessedAt()).isNotNull();
    }

    @Test
    void shouldRejectInvalidProcessedEventData() {
        UUID eventId = UUID.randomUUID();

        assertThatThrownBy(() -> ProcessedEvent.create(null, "type", "consumer")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, null, "consumer")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, " ", "consumer")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, "x".repeat(121), "consumer")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, "type", null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, "type", " ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessedEvent.create(eventId, "type", "x".repeat(121))).isInstanceOf(IllegalArgumentException.class);
    }
}
