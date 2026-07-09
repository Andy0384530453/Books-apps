package com.example.demo.entity;

import com.example.demo.entity.enums.FormatType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "book_copy")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookCopy {

  @Id
  @Column(name = "book-copy-uuid", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book-uuid", nullable = false)
  private Book book;

  @Enumerated(EnumType.STRING)
  @Column(name = "format", nullable = false)
  private FormatType format;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;
  
  @Column(name = "current-stock")
  private Integer currentStock;

  @Column(name = "purchase-price", precision = 10, scale = 2)
  private BigDecimal purchasePrice;

  @Column(name = "selling-price", precision = 10, scale = 2)
  private BigDecimal sellingPrice;
}
