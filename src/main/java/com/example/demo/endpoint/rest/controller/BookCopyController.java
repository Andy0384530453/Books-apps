package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.response.BookCopyRevenues;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.service.BookCopyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/BookCopy")
@AllArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @GetMapping("/revenues")
    public ResponseEntity<List<BookCopyRevenues>> getRevenueByFormat() {
        return ResponseEntity.ok(bookCopyService.getRevenueByFormat());
    }

    @GetMapping("/revenues/total")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        return ResponseEntity.ok(bookCopyService.getTotalRevenue());
    }

    @GetMapping("/revenues/{format}")
    public ResponseEntity<BigDecimal> getRevenueByFormat(@PathVariable FormatType format) {
        return ResponseEntity.ok(bookCopyService.getRevenueByFormat(format));
    }
}
