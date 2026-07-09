package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.response.BookCopyRevenues;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.repository.BookCopyRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock private BookCopyRepository bookCopyRepository;

  private BookCopyService bookCopyService;

  @BeforeEach
  void setUp() {
    bookCopyService = new BookCopyService(bookCopyRepository);
  }

  @Test
  void getRevenueByFormat_shouldReturnEnrichedRevenues() {
    BookCopyRevenues pocheRev = new BookCopyRevenues(FormatType.POCHE, BigDecimal.valueOf(100));
    BookCopyRevenues brocheRev = new BookCopyRevenues(FormatType.BROCHE, BigDecimal.valueOf(300));

    when(bookCopyRepository.getRevenueByFormat()).thenReturn(List.of(pocheRev, brocheRev));
    when(bookCopyRepository.getRevenueSince(any(LocalDateTime.class)))
        .thenReturn(BigDecimal.valueOf(200), BigDecimal.valueOf(800));

    List<BookCopyRevenues> result = bookCopyService.getRevenueByFormat();

    assertEquals(2, result.size());

    BookCopyRevenues poche =
        result.stream().filter(r -> r.getFormat() == FormatType.POCHE).findFirst().orElseThrow();

    assertEquals("Nomena", poche.getTracedBy());
    assertEquals(BigDecimal.valueOf(100), poche.getTotalRevenue());
    assertEquals(0, BigDecimal.valueOf(50).compareTo(poche.getMonthlyRevenue()));
    assertEquals(0, BigDecimal.valueOf(200).compareTo(poche.getAnnualRevenue()));

    verify(bookCopyRepository).getRevenueByFormat();
    verify(bookCopyRepository, times(2)).getRevenueSince(any(LocalDateTime.class));
  }

  @Test
  void getRevenueByFormat_shouldReturnEmptyWhenNoData() {
    when(bookCopyRepository.getRevenueByFormat()).thenReturn(List.of());

    List<BookCopyRevenues> result = bookCopyService.getRevenueByFormat();

    assertTrue(result.isEmpty());
    verify(bookCopyRepository).getRevenueByFormat();
  }

  @Test
  void getRevenueByFormat_shouldHandleSingleFormat() {
    BookCopyRevenues rev = new BookCopyRevenues(FormatType.RELIE, BigDecimal.valueOf(500));

    when(bookCopyRepository.getRevenueByFormat()).thenReturn(List.of(rev));
    when(bookCopyRepository.getRevenueSince(any(LocalDateTime.class)))
        .thenReturn(BigDecimal.valueOf(100), BigDecimal.valueOf(400));

    List<BookCopyRevenues> result = bookCopyService.getRevenueByFormat();

    assertEquals(1, result.size());
    BookCopyRevenues relie = result.getFirst();
    assertEquals("Nomena", relie.getTracedBy());
    assertEquals(0, BigDecimal.valueOf(100).compareTo(relie.getMonthlyRevenue()));
    assertEquals(0, BigDecimal.valueOf(400).compareTo(relie.getAnnualRevenue()));
  }

  @Test
  void getRevenueByFormat_shouldReturnFormatRevenue() {
    when(bookCopyRepository.getRevenueByFormat(FormatType.POCHE))
        .thenReturn(BigDecimal.valueOf(150));

    BigDecimal result = bookCopyService.getRevenueByFormat(FormatType.POCHE);

    assertEquals(0, BigDecimal.valueOf(150).compareTo(result));
    verify(bookCopyRepository).getRevenueByFormat(FormatType.POCHE);
  }

  @Test
  void getTotalRevenue_shouldReturnSum() {
    when(bookCopyRepository.getTotalRevenue()).thenReturn(BigDecimal.valueOf(1000));

    BigDecimal result = bookCopyService.getTotalRevenue();

    assertEquals(0, BigDecimal.valueOf(1000).compareTo(result));
    verify(bookCopyRepository).getTotalRevenue();
  }
}
