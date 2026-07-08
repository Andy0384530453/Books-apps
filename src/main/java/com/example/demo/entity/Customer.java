package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

  @Id
  @Column(name = "customer-uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "first-name", nullable = false)
  private String firstName;

  @Column(name = "last-name", nullable = false)
  private String lastName;

  @Column(name = "phone-number")
  private String phoneNumber;

  @Column(name = "email")
  private String email;
}
