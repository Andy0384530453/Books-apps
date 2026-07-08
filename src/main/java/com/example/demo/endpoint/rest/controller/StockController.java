package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.StockMovementInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.StockService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books/{bookId}")
@AllArgsConstructor
public class StockController {

  private final StockService stockService;

  @PostMapping("/stock-in")
  public ResponseEntity<?> stockIn(
      @PathVariable UUID bookId, @Valid @RequestBody StockMovementInput input) {
    try {
      BookDetail detail = stockService.stockIn(bookId, input.getQuantity(), input.getReason());
      return ResponseEntity.ok(detail);
    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }

  @PostMapping("/stock-out")
  public ResponseEntity<?> stockOut(
      @PathVariable UUID bookId, @Valid @RequestBody StockMovementInput input) {
    try {
      BookDetail detail = stockService.stockOut(bookId, input.getQuantity(), input.getReason());
      return ResponseEntity.ok(detail);
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
