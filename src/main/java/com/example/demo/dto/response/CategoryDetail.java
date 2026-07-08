package com.example.demo.dto.response;

import com.example.demo.entity.enums.CategoryName;
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
public class CategoryDetail {

  @JsonProperty("id_category")
  private UUID idCategory;

  @JsonProperty("category_name")
  private CategoryName categoryName;
}
