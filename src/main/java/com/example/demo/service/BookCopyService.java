package com.example.demo.service;

import com.example.demo.dto.response.StockByFormatEntry;
import com.example.demo.dto.response.StockResponse;
import com.example.demo.dto.response.StockUpdateResponse;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.StockMovement;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.entity.enums.MovementType;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookCopyService {

  private final BookCopyRepository bookCopyRepository;
  private final BookRepository bookRepository;
  private final StockMovementRepository stockMovementRepository;

  public Object getStock(UUID bookId, FormatType format) {
    if (format != null) {
      Integer stock =
          bookCopyRepository
              .findByBookIdAndFormat(bookId, format)
              .map(BookCopy::getQuantity)
              .orElse(0);
      return StockResponse.builder().currentStock(stock).build();
    }
    List<BookCopy> copies = bookCopyRepository.findByBookId(bookId);
    return copies.stream()
        .map(c -> StockByFormatEntry.builder().format(c.getFormat()).stock(c.getQuantity()).build())
        .toList();
  }

  @Transactional
  public StockUpdateResponse increment(UUID bookId, UUID copyId, Integer quantity) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
    BookCopy copy =
        bookCopyRepository
            .findById(copyId)
            .orElseThrow(() -> new ResourceNotFoundException("BookCopy not found: " + copyId));
    if (!copy.getBook().getId().equals(bookId)) {
      throw new InvalidRequestException("BookCopy does not belong to the specified book");
    }

    copy.setQuantity(copy.getQuantity() + quantity);
    bookCopyRepository.save(copy);
    syncCurrentStock(book);
    recordMovement(book, MovementType.IN, quantity, "Restock");

    return StockUpdateResponse.builder()
        .bookCopyId(copy.getId())
        .newStock(copy.getQuantity())
        .build();
  }

  @Transactional
  public StockUpdateResponse decrement(UUID bookId, UUID copyId, Integer quantity) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
    BookCopy copy =
        bookCopyRepository
            .findById(copyId)
            .orElseThrow(() -> new ResourceNotFoundException("BookCopy not found: " + copyId));
    if (!copy.getBook().getId().equals(bookId)) {
      throw new InvalidRequestException("BookCopy does not belong to the specified book");
    }
    if (copy.getQuantity() < quantity) {
      throw new InvalidRequestException("Insufficient stock");
    }

    copy.setQuantity(copy.getQuantity() - quantity);
    bookCopyRepository.save(copy);
    syncCurrentStock(book);
    recordMovement(book, MovementType.OUT, quantity, "Sale");

    return StockUpdateResponse.builder()
        .bookCopyId(copy.getId())
        .newStock(copy.getQuantity())
        .build();
  }

  private void syncCurrentStock(Book book) {
    Integer total = bookCopyRepository.sumQuantityByBookId(book.getId());
    book.setCurrentStock(total);
    bookRepository.save(book);
  }

  private void recordMovement(Book book, MovementType type, Integer quantity, String reason) {
    StockMovement movement =
        StockMovement.builder()
            .id(UUID.randomUUID())
            .book(book)
            .movementType(type)
            .movementDate(LocalDateTime.now())
            .quantity(quantity)
            .reason(reason)
            .build();
    stockMovementRepository.save(movement);
  }
}
