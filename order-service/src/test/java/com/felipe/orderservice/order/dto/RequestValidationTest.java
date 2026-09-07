package com.felipe.orderservice.order.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RequestValidationTest {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    void validCreateOrderRequestShouldHaveNoViolations() {
        CreateOrderRequest request = new CreateOrderRequest(
                UUID.randomUUID(),
                List.of(new CreateOrderItemRequest(UUID.randomUUID(), "Phone", BigDecimal.TEN, 1))
        );

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }

    @Test
    void shouldValidateCreateOrderRequestAndNestedItems() {
        CreateOrderRequest request = new CreateOrderRequest(
                null,
                List.of(new CreateOrderItemRequest(null, " ", new BigDecimal("-0.01"), 0))
        );

        assertThat(VALIDATOR.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder(
                        "customerId",
                        "items[0].productId",
                        "items[0].productName",
                        "items[0].unitPrice",
                        "items[0].quantity"
                );
    }

    @Test
    void shouldRejectEmptyItems() {
        CreateOrderRequest request = new CreateOrderRequest(UUID.randomUUID(), List.of());

        assertThat(VALIDATOR.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("items");
    }

    @Test
    void shouldValidateCancellationReason() {
        assertThat(VALIDATOR.validate(new CancelOrderRequest("CUSTOMER_REQUEST"))).isEmpty();
        assertThat(VALIDATOR.validate(new CancelOrderRequest(" ")))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("reason");
    }
}
