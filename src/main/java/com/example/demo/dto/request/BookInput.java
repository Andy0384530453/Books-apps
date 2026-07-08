package com.example.demo.dto.request;

import com.example.demo.dto.response.CategoryDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
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
public class BookInput {

  @NotBlank(message = "title is required")
  private String title;

  @NotNull(message = "purchasePrice is required")
  @Positive(message = "purchasePrice must be positive")
  private BigDecimal purchasePrice;

  @NotNull(message = "sellingPrice is required")
  @Positive(message = "sellingPrice must be positive")
  private BigDecimal sellingPrice;

  @NotNull(message = "category is required")
  @Valid
  private CategoryDetail category;
}
