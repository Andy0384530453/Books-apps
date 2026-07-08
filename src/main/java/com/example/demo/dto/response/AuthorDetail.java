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
public class AuthorDetail {

  @JsonProperty("id_author")
  private UUID idAuthor;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("first_name")
  private String firstName;
}
