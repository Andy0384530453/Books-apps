package com.example.demo.dto.request;

import com.example.demo.dto.response.CategoryDetail;
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

  private String title;

  private BigDecimal purchasePrice;

  private BigDecimal sellingPrice;

  private CategoryDetail category;
}
