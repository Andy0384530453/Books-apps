package com.example.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.dto.request.BookInput;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.dto.response.CategoryDetail;
import com.example.demo.entity.enums.CategoryName;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private BookService bookService;

  @Test
  void getAllBooks_shouldReturn200() throws Exception {
    when(bookService.getAllBooks(null))
        .thenReturn(List.of(BookDetail.builder().title("1984").build()));

    mockMvc
        .perform(get("/api/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("1984"));
  }

  @Test
  void getAllBooks_shouldPassGenreParam() throws Exception {
    when(bookService.getAllBooks("Dystopia"))
        .thenReturn(List.of(BookDetail.builder().title("1984").build()));

    mockMvc.perform(get("/api/books").param("genre", "Dystopia")).andExpect(status().isOk());
  }

  @Test
  void getAllBooks_shouldReturn500OnError() throws Exception {
    when(bookService.getAllBooks(null)).thenThrow(new RuntimeException("DB down"));

    mockMvc
        .perform(get("/api/books"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
  }

  @Test
  void createBook_shouldReturn201() throws Exception {
    BookInput input =
        BookInput.builder()
            .title("New Book")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(20))
            .category(
                CategoryDetail.builder()
                    .idCategory(UUID.randomUUID())
                    .categoryName(CategoryName.FICTION)
                    .build())
            .build();
    when(bookService.createBook(any(BookInput.class)))
        .thenReturn(BookDetail.builder().title("New Book").build());

    mockMvc
        .perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("New Book"));
  }

  @Test
  void createBook_shouldReturn400OnInvalidRequest() throws Exception {
    BookInput input =
        BookInput.builder()
            .title("New Book")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(20))
            .category(
                CategoryDetail.builder()
                    .idCategory(UUID.randomUUID())
                    .categoryName(CategoryName.FICTION)
                    .build())
            .build();
    when(bookService.createBook(any(BookInput.class)))
        .thenThrow(new InvalidRequestException("category is required"));

    mockMvc
        .perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
  }

  @Test
  void createBook_shouldReturn400WhenBodyInvalid() throws Exception {
    mockMvc
        .perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createBook_shouldReturn500OnError() throws Exception {
    BookInput input =
        BookInput.builder()
            .title("New Book")
            .purchasePrice(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(20))
            .category(
                CategoryDetail.builder()
                    .idCategory(UUID.randomUUID())
                    .categoryName(CategoryName.FICTION)
                    .build())
            .build();
    when(bookService.createBook(any(BookInput.class)))
        .thenThrow(new RuntimeException("Unexpected"));

    mockMvc
        .perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
  }

  @Test
  void getLowStockBooks_shouldReturn200() throws Exception {
    when(bookService.getLowStockBooks())
        .thenReturn(List.of(BookDetail.builder().title("Low").currentStock(2).build()));

    mockMvc
        .perform(get("/api/books/low-stock"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].current_stock").value(2));
  }

  @Test
  void getLowStockBooks_shouldReturn500OnError() throws Exception {
    when(bookService.getLowStockBooks()).thenThrow(new RuntimeException("DB down"));

    mockMvc
        .perform(get("/api/books/low-stock"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
  }

  @Test
  void getTopProfitBooks_shouldReturn200() throws Exception {
    when(bookService.getTopProfitBooks())
        .thenReturn(
            List.of(
                BookProfitDetail.builder()
                    .book(BookDetail.builder().title("Best").build())
                    .totalProfit(BigDecimal.valueOf(100))
                    .build()));

    mockMvc
        .perform(get("/api/books/top-profit"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].total_profit").value(100));
  }

  @Test
  void getTopProfitBooks_shouldReturn500OnError() throws Exception {
    when(bookService.getTopProfitBooks()).thenThrow(new RuntimeException("DB down"));

    mockMvc
        .perform(get("/api/books/top-profit"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
  }
}
