package com.example.demo.dto.response;

import com.example.demo.entity.enums.FormatType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookCopyRevenues {
   private BigDecimal revenues;
   private FormatType format;

}
