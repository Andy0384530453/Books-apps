package com.example.demo.repository;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.enums.FormatType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {

  @Query(
      """
          SELECT new com.example.demo.dto.RevenueByFormatDTO(
              bc.format,
              SUM(bc.sellingPrice * bc.quantity)
          )
          FROM BookCopy bc
          GROUP BY bc.format
      """)
  List<BookCopyRevenues> getRevenueByFormat();

  @Query(
      """
          SELECT COALESCE(SUM(bc.sellingPrice * bc.quantity), 0)
          FROM BookCopy bc
          WHERE bc.format = :format
      """)
  BigDecimal getRevenueByFormat(FormatType format);

  @Query(
      """
          SELECT COALESCE(SUM(bc.sellingPrice * bc.quantity), 0)
          FROM BookCopy bc
      """)
  BigDecimal getTotalRevenue();
}
