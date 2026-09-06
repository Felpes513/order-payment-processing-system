package com.felipe.orderservice.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/orders/123");
    }

    @Test
    void shouldHandleResourceNotFound() {
        ErrorResponse response = handler.handleResourceNotFound(
                new ResourceNotFoundException("Order not found"), request
        );

        assertThat(response.timestamp()).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.error()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
        assertThat(response.message()).isEqualTo("Order not found");
        assertThat(response.path()).isEqualTo("/orders/123");
    }

    @Test
    void shouldHandleBusinessException() {
        ResponseEntity<ErrorResponse> entity = handler.handleBusinessException(
                new BusinessException("Order is already cancelled"), request
        );

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(entity.getBody()).isNotNull().satisfies(response -> {
            assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT.value());
            assertThat(response.message()).isEqualTo("Order is already cancelled");
            assertThat(response.path()).isEqualTo("/orders/123");
        });
    }

    @Test
    void shouldHandleValidationErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "customerId", "Customer id is required"));
        bindingResult.addError(new FieldError("request", "items", "Order must have at least one item"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                mock(MethodParameter.class), bindingResult
        );

        ResponseEntity<ValidationErrorResponse> entity = handler.handleValidationErrors(exception, request);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody()).isNotNull().satisfies(response -> {
            assertThat(response.timestamp()).isNotNull();
            assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.message()).isEqualTo("Validation failed");
            assertThat(response.path()).isEqualTo("/orders/123");
            assertThat(response.fieldErrors())
                    .containsExactly(
                            new FieldErrorResponse("customerId", "Customer id is required"),
                            new FieldErrorResponse("items", "Order must have at least one item")
                    );
        });
    }
}
