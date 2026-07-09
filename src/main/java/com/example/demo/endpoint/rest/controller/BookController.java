package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.request.BookInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.service.BookService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping("/books")
  public ResponseEntity<?> getAllBooks(@RequestParam(required = false) String genre) {
    try {
      List<BookDetail> books = bookService.getAllBooks(genre);
      return ResponseEntity.ok(books);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }

  @PostMapping("/books")
  public ResponseEntity<?> createBook(@RequestBody BookInput input) {
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

  @GetMapping("/books/low-stock")
  public ResponseEntity<?> getLowStockBooks() {
    try {
      List<BookDetail> books = bookService.getLowStockBooks();
      return ResponseEntity.ok(books);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }

  @GetMapping("/books/top-profit")
  public ResponseEntity<?> getTopProfitBooks() {
    try {
      List<BookProfitDetail> books = bookService.getTopProfitBooks();
      return ResponseEntity.ok(books);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
  }
}
