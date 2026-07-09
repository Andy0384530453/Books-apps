package com.example.demo.endpoint.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.dto.response.BookCopyRevenues;
import com.example.demo.entity.enums.FormatType;
import com.example.demo.service.BookCopyService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookCopyController.class)
class BookCopyControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookCopyService bookCopyService;

  @Test
  void getRevenueByFormat_shouldReturn200() throws Exception {
    BookCopyRevenues rev = new BookCopyRevenues(FormatType.POCHE, BigDecimal.valueOf(100));
    rev.setMonthlyRevenue(BigDecimal.valueOf(50));
    rev.setAnnualRevenue(BigDecimal.valueOf(200));
    rev.setTracedBy("Nomena");

    when(bookCopyService.getRevenueByFormat()).thenReturn(List.of(rev));

    mockMvc
        .perform(get("/api/BookCopy/revenues"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].format").value("POCHE"))
        .andExpect(jsonPath("$[0].total_revenue").value(100))
        .andExpect(jsonPath("$[0].monthly_revenue").value(50))
        .andExpect(jsonPath("$[0].annual_revenue").value(200))
        .andExpect(jsonPath("$[0].traced_by").value("Nomena"));
  }

  @Test
  void getRevenueByFormat_shouldReturn500OnError() throws Exception {
    when(bookCopyService.getRevenueByFormat()).thenThrow(new RuntimeException("DB down"));

    mockMvc
        .perform(get("/api/BookCopy/revenues"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
  }

  @Test
  void getTotalRevenue_shouldReturn200() throws Exception {
    when(bookCopyService.getTotalRevenue()).thenReturn(BigDecimal.valueOf(500));

    mockMvc
        .perform(get("/api/BookCopy/revenues/total"))
        .andExpect(status().isOk())
        .andExpect(content().string("500"));
  }

  @Test
  void getRevenueByFormat_withFormatParam_shouldReturn200() throws Exception {
    when(bookCopyService.getRevenueByFormat(FormatType.BROCHE)).thenReturn(BigDecimal.valueOf(300));

    mockMvc
        .perform(get("/api/BookCopy/revenues/BROCHE"))
        .andExpect(status().isOk())
        .andExpect(content().string("300"));
  }
}
