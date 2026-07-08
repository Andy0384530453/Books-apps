package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Author {

  @Id
  @Column(name = "author-uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "last-name", nullable = false)
  private String lastName;

  @Column(name = "first-name", nullable = false)
  private String firstName;
}
