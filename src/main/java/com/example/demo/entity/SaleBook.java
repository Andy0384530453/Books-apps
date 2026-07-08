package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "sale_book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaleBook {

  @Id
  @Column(name = "sale-book-uuid", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sale-uuid", nullable = false)
  private Sale sale;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book-uuid", nullable = false)
  private Book book;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;
}
