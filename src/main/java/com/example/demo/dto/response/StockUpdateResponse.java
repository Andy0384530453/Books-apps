package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class StockUpdateResponse {

  @JsonProperty("book_copy_id")
  private UUID bookCopyId;

  @JsonProperty("new_stock")
  private Integer newStock;
}
