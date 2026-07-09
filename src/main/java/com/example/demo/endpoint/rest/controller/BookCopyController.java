package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.StockRequest;
import com.example.demo.dto.response.StockUpdateResponse;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.BookCopyService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookCopyController {

  private final BookCopyService bookCopyService;

  @GetMapping("/books/{bookId}/copies/stock")
  public ResponseEntity<?> getStock(
      @PathVariable UUID bookId, @RequestParam(required = false) FormatType format) {
    try {
      Object result = bookCopyService.getStock(bookId, format);
      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }

  @PostMapping("/books/{bookId}/copies/{copyId}/increment")
  public ResponseEntity<?> incrementStock(
      @PathVariable UUID bookId,
      @PathVariable UUID copyId,
      @Valid @RequestBody StockRequest request) {
    try {
      StockUpdateResponse result = bookCopyService.increment(bookId, copyId, request.getQuantity());
      return ResponseEntity.ok(result);
    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
    } catch (InvalidRequestException ex) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "BAD_REQUEST", "message", ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }

  @PostMapping("/books/{bookId}/copies/{copyId}/decrement")
  public ResponseEntity<?> decrementStock(
      @PathVariable UUID bookId,
      @PathVariable UUID copyId,
      @Valid @RequestBody StockRequest request) {
    try {
      StockUpdateResponse result = bookCopyService.decrement(bookId, copyId, request.getQuantity());
      return ResponseEntity.ok(result);
    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
    } catch (InvalidRequestException ex) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "BAD_REQUEST", "message", ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }
}
