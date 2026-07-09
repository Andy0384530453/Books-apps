package com.example.demo.service;

import com.example.demo.dto.response.BookCopyRevenues;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.repository.BookCopyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BookCopyService {
    private final BookCopyRepository bookCopyRepository;


    @Transactional
    public List<BookCopyRevenues> getRevenueByFormat() {
        return bookCopyRepository.getRevenueByFormat();
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
