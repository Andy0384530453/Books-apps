package com.example.demo.service;

import com.example.demo.dto.request.BookInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.dto.response.CategoryDetail;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.SaleBookRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final SaleBookRepository saleBookRepository;
  private final BookMapper bookMapper;

  @Transactional
  public List<BookDetail> getAllBooks(String genre) {
    List<Book> books;
    if (genre != null && !genre.isBlank()) {
      books = bookRepository.findByGenre(genre);
    } else {
      books = bookRepository.findAll();
    }
    return books.stream().map(bookMapper::toDetail).toList();
  }

  @Transactional
  public BookDetail createBook(BookInput input) {
    if (input.getTitle() == null || input.getTitle().isBlank()) {
      throw new InvalidRequestException("title must not be blank");
    }
    if (input.getPurchasePrice() == null
        || input.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidRequestException("purchasePrice must be positive");
    }
    if (input.getSellingPrice() == null
        || input.getSellingPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidRequestException("sellingPrice must be positive");
    }
    if (input.getCategory() == null) {
      throw new InvalidRequestException("category is required");
    }

    Category category = resolveCategory(input.getCategory());

    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title(input.getTitle().trim())
            .genre(null)
            .publicationDate(null)
            .purchasePrice(input.getPurchasePrice())
            .sellingPrice(input.getSellingPrice())
            .currentStock(0)
            .category(category)
            .authors(new HashSet<>())
            .build();

    return bookMapper.toDetail(bookRepository.save(book));
  }

  @Transactional
  public List<BookDetail> getLowStockBooks() {
    return bookRepository.findByCurrentStockLessThanEqual(3).stream()
        .map(bookMapper::toDetail)
        .toList();
  }

  @Transactional
  public List<BookProfitDetail> getTopProfitBooks() {
    return bookRepository.findAll().stream()
        .map(
            book -> {
              Integer sold = saleBookRepository.sumQuantityByBookId(book.getId());
              BigDecimal margin = book.getSellingPrice().subtract(book.getPurchasePrice());
              return bookMapper.toProfitDetail(book, margin.multiply(BigDecimal.valueOf(sold)));
            })
        .filter(d -> d.getTotalProfit().compareTo(BigDecimal.ZERO) > 0)
        .sorted(Comparator.comparing(BookProfitDetail::getTotalProfit).reversed())
        .toList();
  }

  private Category resolveCategory(CategoryDetail dto) {
    if (dto.getIdCategory() != null) {
      return Category.builder()
          .id(dto.getIdCategory())
          .categoryName(dto.getCategoryName() == null ? null : dto.getCategoryName().name())
          .build();
    }
    if (dto.getCategoryName() != null) {
      return Category.builder()
          .id(UUID.randomUUID())
          .categoryName(dto.getCategoryName().name())
          .build();
    }
    throw new InvalidRequestException("category must have an id or name");
  }
}
