package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.BookInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.dto.response.CategoryDetail;
import com.example.demo.entity.Book;
import com.example.demo.entity.enums.CategoryName;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.SaleBookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepository bookRepository;
  @Mock private SaleBookRepository saleBookRepository;
  @Mock private BookMapper bookMapper;

  private BookService bookService;

  @BeforeEach
  void setUp() {
    bookService = new BookService(bookRepository, saleBookRepository, bookMapper);
  }

  @Test
  void getAllBooks_shouldReturnAllWhenGenreNull() {
    Book book = Book.builder().id(UUID.randomUUID()).title("1984").build();
    BookDetail detail = BookDetail.builder().idBook(book.getId()).title("1984").build();
    when(bookRepository.findAll()).thenReturn(List.of(book));
    when(bookMapper.toDetail(book)).thenReturn(detail);

    List<BookDetail> result = bookService.getAllBooks(null);

    assertEquals(1, result.size());
    assertEquals("1984", result.getFirst().getTitle());
    verify(bookRepository).findAll();
    verifyNoMoreInteractions(bookRepository);
  }

  @Test
  void getAllBooks_shouldFilterByGenre() {
    Book book = Book.builder().id(UUID.randomUUID()).title("1984").genre("Dystopia").build();
    BookDetail detail = BookDetail.builder().idBook(book.getId()).title("1984").build();
    when(bookRepository.findByGenre("Dystopia")).thenReturn(List.of(book));
    when(bookMapper.toDetail(book)).thenReturn(detail);

    List<BookDetail> result = bookService.getAllBooks("Dystopia");

    assertEquals(1, result.size());
    verify(bookRepository).findByGenre("Dystopia");
  }

  @Test
  void getAllBooks_shouldIgnoreBlankGenre() {
    Book book = Book.builder().id(UUID.randomUUID()).title("1984").build();
    when(bookRepository.findAll()).thenReturn(List.of(book));
    when(bookMapper.toDetail(book))
        .thenReturn(BookDetail.builder().idBook(book.getId()).title("1984").build());

    bookService.getAllBooks("   ");
    verify(bookRepository).findAll();
  }

  @Test
  void createBook_shouldSaveAndReturn() {
    CategoryDetail catDetail =
        CategoryDetail.builder()
            .idCategory(UUID.randomUUID())
            .categoryName(CategoryName.FICTION)
            .build();
    BookInput input =
        BookInput.builder()
            .title("  New Book  ")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(20))
            .category(catDetail)
            .build();
    when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));
    when(bookMapper.toDetail(any(Book.class)))
        .thenReturn(BookDetail.builder().title("New Book").build());

    BookDetail result = bookService.createBook(input);

    assertEquals("New Book", result.getTitle());
    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void createBook_shouldThrowWhenTitleBlank() {
    BookInput input = BookInput.builder().title("   ").build();
    assertThrows(InvalidRequestException.class, () -> bookService.createBook(input));
  }

  @Test
  void createBook_shouldThrowWhenPurchasePriceNull() {
    BookInput input = BookInput.builder().title("Test").purchasePrice(null).build();
    assertThrows(InvalidRequestException.class, () -> bookService.createBook(input));
  }

  @Test
  void createBook_shouldThrowWhenPurchasePriceNotPositive() {
    BookInput input = BookInput.builder().title("Test").purchasePrice(BigDecimal.ZERO).build();
    assertThrows(InvalidRequestException.class, () -> bookService.createBook(input));
  }

  @Test
  void createBook_shouldThrowWhenSellingPriceNull() {
    BookInput input =
        BookInput.builder().title("Test").purchasePrice(BigDecimal.TEN).sellingPrice(null).build();
    assertThrows(InvalidRequestException.class, () -> bookService.createBook(input));
  }

  @Test
  void createBook_shouldThrowWhenCategoryNull() {
    BookInput input =
        BookInput.builder()
            .title("Test")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(20))
            .category(null)
            .build();
    assertThrows(InvalidRequestException.class, () -> bookService.createBook(input));
  }

  @Test
  void getLowStockBooks_shouldReturnBooksWithStockLessThanOrEqual3() {
    Book book = Book.builder().id(UUID.randomUUID()).title("Low Stock").currentStock(2).build();
    when(bookRepository.findByCurrentStockLessThanEqual(3)).thenReturn(List.of(book));
    when(bookMapper.toDetail(book))
        .thenReturn(
            BookDetail.builder().idBook(book.getId()).title("Low Stock").currentStock(2).build());

    List<BookDetail> result = bookService.getLowStockBooks();

    assertEquals(1, result.size());
    assertEquals("Low Stock", result.getFirst().getTitle());
  }

  @Test
  void getLowStockBooks_shouldReturnEmptyListWhenNoneLow() {
    when(bookRepository.findByCurrentStockLessThanEqual(3)).thenReturn(List.of());
    assertTrue(bookService.getLowStockBooks().isEmpty());
  }

  @Test
  void getTopProfitBooks_shouldReturnSortedByProfitDesc() {
    UUID bookId1 = UUID.randomUUID();
    UUID bookId2 = UUID.randomUUID();

    Book book1 =
        Book.builder()
            .id(bookId1)
            .title("High Profit")
            .purchasePrice(BigDecimal.valueOf(5))
            .sellingPrice(BigDecimal.valueOf(15))
            .currentStock(10)
            .build();
    Book book2 =
        Book.builder()
            .id(bookId2)
            .title("Low Profit")
            .purchasePrice(BigDecimal.valueOf(10))
            .sellingPrice(BigDecimal.valueOf(12))
            .currentStock(5)
            .build();

    when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
    when(saleBookRepository.sumQuantityByBookId(bookId1)).thenReturn(10);
    when(saleBookRepository.sumQuantityByBookId(bookId2)).thenReturn(5);
    when(bookMapper.toProfitDetail(book1, BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(10))))
        .thenReturn(
            BookProfitDetail.builder()
                .book(BookDetail.builder().title("High Profit").build())
                .totalProfit(BigDecimal.valueOf(100))
                .build());
    when(bookMapper.toProfitDetail(book2, BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(5))))
        .thenReturn(
            BookProfitDetail.builder()
                .book(BookDetail.builder().title("Low Profit").build())
                .totalProfit(BigDecimal.TEN)
                .build());

    List<BookProfitDetail> result = bookService.getTopProfitBooks();

    assertEquals(2, result.size());
    assertEquals("High Profit", result.get(0).getBook().getTitle());
    assertEquals("Low Profit", result.get(1).getBook().getTitle());
  }

  @Test
  void getTopProfitBooks_shouldFilterZeroProfit() {
    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title("No Sales")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.TEN)
            .currentStock(5)
            .build();
    when(bookRepository.findAll()).thenReturn(List.of(book));
    when(saleBookRepository.sumQuantityByBookId(book.getId())).thenReturn(0);
    when(bookMapper.toProfitDetail(book, BigDecimal.ZERO))
        .thenReturn(
            BookProfitDetail.builder()
                .book(BookDetail.builder().title("No Sales").build())
                .totalProfit(BigDecimal.ZERO)
                .build());

    assertTrue(bookService.getTopProfitBooks().isEmpty());
  }
}
