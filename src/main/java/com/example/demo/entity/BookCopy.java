package com.example.demo.entity;

import com.example.demo.entity.enums.FormatType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
}
