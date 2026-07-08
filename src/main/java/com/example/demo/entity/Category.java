package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
