package com.example.demo.entity;

import com.example.demo.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "sale")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Sale {

  @Id
  @Column(name = "sale-uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "sale-date", nullable = false)
  private LocalDateTime saleDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment-status", nullable = false)
  private PaymentStatus paymentStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer-uuid", nullable = false)
  private Customer customer;

  @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<SaleBook> books = new ArrayList<>();
}
