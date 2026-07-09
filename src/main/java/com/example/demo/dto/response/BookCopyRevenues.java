package com.example.demo.dto.response;

import com.example.demo.entity.enums.FormatType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCopyRevenues {
  private FormatType format;

  @JsonProperty("total_revenue")
  private BigDecimal totalRevenue;

  @JsonProperty("monthly_revenue")
  private BigDecimal monthlyRevenue;

  @JsonProperty("annual_revenue")
  private BigDecimal annualRevenue;

  @JsonProperty("traced_by")
  private String tracedBy;

  public BookCopyRevenues(FormatType format, BigDecimal totalRevenue) {
    this.format = format;
    this.totalRevenue = totalRevenue;
    this.monthlyRevenue = BigDecimal.ZERO;
    this.annualRevenue = BigDecimal.ZERO;
    this.tracedBy = "Nomena";
  }
}
