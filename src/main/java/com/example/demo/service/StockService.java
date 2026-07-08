package com.example.demo.service;

import com.example.demo.dto.response.BookDetail;
import com.example.demo.entity.Book;
import com.example.demo.entity.StockMovement;
import com.example.demo.entity.enums.MovementType;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockService {

  private final BookRepository bookRepository;
  private final StockMovementRepository stockMovementRepository;
  private final BookMapper bookMapper;

  @Transactional
  public BookDetail stockIn(UUID bookId, Integer quantity, String reason) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

    book.setCurrentStock(book.getCurrentStock() + quantity);
    bookRepository.save(book);

    StockMovement movement =
        StockMovement.builder()
            .id(UUID.randomUUID())
            .book(book)
            .movementType(MovementType.IN)
            .movementDate(LocalDateTime.now())
            .quantity(quantity)
            .reason(reason)
            .build();
    stockMovementRepository.save(movement);

    return bookMapper.toDetail(book);
  }

  @Transactional
  public BookDetail stockOut(UUID bookId, Integer quantity, String reason) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

    if (book.getCurrentStock() < quantity) {
      throw new InvalidRequestException(
          "Insufficient stock: current=" + book.getCurrentStock() + ", requested=" + quantity);
    }

    book.setCurrentStock(book.getCurrentStock() - quantity);
    bookRepository.save(book);

    StockMovement movement =
        StockMovement.builder()
            .id(UUID.randomUUID())
            .book(book)
            .movementType(MovementType.OUT)
            .movementDate(LocalDateTime.now())
            .quantity(quantity)
            .reason(reason)
            .build();
    stockMovementRepository.save(movement);

    return bookMapper.toDetail(book);
  }
}
