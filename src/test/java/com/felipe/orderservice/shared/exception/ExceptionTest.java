package com.felipe.orderservice.shared.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void exceptionsShouldPreserveMessage() {
        assertThat(new BusinessException("business error")).hasMessage("business error");
        assertThat(new ResourceNotFoundException("not found")).hasMessage("not found");
    }
}
