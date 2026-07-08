package com.example.demo.repository;

import com.example.demo.entity.SaleBook;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleBookRepository extends JpaRepository<SaleBook, UUID> {

  @Query("SELECT COALESCE(SUM(sb.quantity), 0) FROM SaleBook sb WHERE sb.book.id = :bookId")
  Integer sumQuantityByBookId(UUID bookId);
}
