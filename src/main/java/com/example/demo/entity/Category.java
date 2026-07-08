package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {

  @Id
  @Column(name = "category-uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "category-name", nullable = false)
  private String categoryName;
}
