package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.response.BookDetail;
import com.example.demo.service.BookService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

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
}
