package ar.edu.utn.frc.tup.lciii.exception;

import ar.edu.utn.frc.tup.lciii.dtos.common.ErrorApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GlobalExceptionHandlerTests {
    static GlobalExceptionHandler handler;

    @BeforeAll
    public static void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleAllExceptions() {
        Exception ex = new Exception("An unexpected error occurred");

        ResponseEntity<ErrorApi> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }


    @Test
    public void testHandleIllegalArgumentException() {
        CountryNotFoundException ex = new CountryNotFoundException("Not found");

        ResponseEntity<ErrorApi> response = handler.handleCountryNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), Objects.requireNonNull(response.getBody()).getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }


}
