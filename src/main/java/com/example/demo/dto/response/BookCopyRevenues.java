package com.example.demo.dto.response;

import com.example.demo.entity.enums.FormatType;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookCopyRevenues {
  private BigDecimal revenues;
  private FormatType format;
}
