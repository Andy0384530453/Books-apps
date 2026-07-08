package com.example.demo.entity;

import com.example.demo.entity.enums.MovementType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "stock_movement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StockMovement {

  @Id
  @Column(name = "stock-movement-uuid", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book-uuid", nullable = false)
  private Book book;

  @Enumerated(EnumType.STRING)
  @Column(name = "movement-type", nullable = false)
  private MovementType movementType;

  @Column(name = "movement-date", nullable = false)
  private LocalDateTime movementDate;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sale-uuid")
  private Sale sale;

  @Column(name = "reason")
  private String reason;
}
