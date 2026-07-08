package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetail {

  @JsonProperty("id_book")
  private UUID idBook;

  private String title;

  private String genre;

  @JsonProperty("publication_date")
  private LocalDate publicationDate;

  @JsonProperty("purchase_price")
  private BigDecimal purchasePrice;

  @JsonProperty("selling_price")
  private BigDecimal sellingPrice;

  @JsonProperty("current_stock")
  private Integer currentStock;

  private CategoryDetail category;

  private List<AuthorDetail> authors;
}
