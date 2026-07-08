package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Book {

  @Id
  @Column(name = "book-uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "genre")
  private String genre;

  @Column(name = "publication-date")
  private LocalDate publicationDate;

  @Column(name = "purchase-price", precision = 10, scale = 2)
  private BigDecimal purchasePrice;

  @Column(name = "selling-price", precision = 10, scale = 2)
  private BigDecimal sellingPrice;

  @Column(name = "current-stock")
  private Integer currentStock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category-uuid")
  private Category category;

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book-uuid"),
      inverseJoinColumns = @JoinColumn(name = "author-uuid"))
  @Builder.Default
  private Set<Author> authors = new HashSet<>();
}
