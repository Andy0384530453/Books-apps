package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.BookInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping("/books")
  public ResponseEntity<?> createBook(@Valid @RequestBody BookInput input) {
    try {
      BookDetail created = bookService.createBook(input);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (InvalidRequestException ex) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "BAD_REQUEST", "message", ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }
}
