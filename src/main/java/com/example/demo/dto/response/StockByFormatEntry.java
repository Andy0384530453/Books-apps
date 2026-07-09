package com.example.demo.dto.response;

import com.example.demo.entity.enums.FormatType;
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
public class StockByFormatEntry {

  private FormatType format;

  private Integer stock;
}
