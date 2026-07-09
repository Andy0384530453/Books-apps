package com.example.demo.service;

import com.example.demo.dto.response.BookCopyRevenues;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.repository.BookCopyRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookCopyService {
  private final BookCopyRepository bookCopyRepository;

  @Transactional
  public List<BookCopyRevenues> getRevenueByFormat() {
    List<BookCopyRevenues> revenues = bookCopyRepository.getRevenueByFormat();
    BigDecimal totalRevenueAllFormats =
        revenues.stream()
            .map(BookCopyRevenues::getTotalRevenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal monthlyTotal =
        bookCopyRepository.getRevenueSince(LocalDateTime.now().minusMonths(1));
    BigDecimal annualTotal = bookCopyRepository.getRevenueSince(LocalDateTime.now().minusYears(1));

    for (BookCopyRevenues rev : revenues) {
      BigDecimal ratio =
          totalRevenueAllFormats.compareTo(BigDecimal.ZERO) > 0
              ? rev.getTotalRevenue().divide(totalRevenueAllFormats, 10, RoundingMode.HALF_UP)
              : BigDecimal.ZERO;
      rev.setMonthlyRevenue(monthlyTotal.multiply(ratio).setScale(2, RoundingMode.HALF_UP));
      rev.setAnnualRevenue(annualTotal.multiply(ratio).setScale(2, RoundingMode.HALF_UP));
      rev.setTracedBy("Nomena");
    }
    return revenues;
  }

  @Transactional
  public BigDecimal getRevenueByFormat(FormatType format) {
    return bookCopyRepository.getRevenueByFormat(format);
  }

  @Transactional
  public BigDecimal getTotalRevenue() {
    return bookCopyRepository.getTotalRevenue();
  }
}
