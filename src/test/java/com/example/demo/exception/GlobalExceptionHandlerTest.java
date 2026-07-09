package com.example.demo.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  void handleNotFound_shouldReturn404() {
    ResourceNotFoundException ex = new ResourceNotFoundException("book not found");
    ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("NOT_FOUND", response.getBody().get("error"));
  }

  @Test
  void handleInvalidRequest_shouldReturn400() {
    InvalidRequestException ex = new InvalidRequestException("invalid");
    ResponseEntity<Map<String, Object>> response = handler.handleInvalidRequest(ex);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("BAD_REQUEST", response.getBody().get("error"));
  }

  @Test
  void handleGeneral_shouldReturn500() {
    Exception ex = new RuntimeException("unexpected");
    ResponseEntity<Map<String, Object>> response = handler.handleGeneral(ex);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("INTERNAL_ERROR", response.getBody().get("error"));
  }

  @Test
  void handleNotReadable_withInvalidFormat_shouldReturn400() {
    InvalidFormatException ife = InvalidFormatException.from(null, "invalid", "abc", Integer.class);
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", ife);
    ResponseEntity<Map<String, Object>> response = handler.handleNotReadable(ex);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("MALFORMED_JSON", response.getBody().get("error"));
  }

  @Test
  void handleNotReadable_withoutCause_shouldReturn400() {
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException("malformed");
    ResponseEntity<Map<String, Object>> response = handler.handleNotReadable(ex);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("MALFORMED_JSON", response.getBody().get("error"));
  }
}
