package com.example.demo.repository;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.enums.FormatType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {

  List<BookCopy> findByBookId(UUID bookId);

  Optional<BookCopy> findByBookIdAndFormat(UUID bookId, FormatType format);

  @Query("SELECT COALESCE(SUM(bc.quantity), 0) FROM BookCopy bc WHERE bc.book.id = :bookId")
  Integer sumQuantityByBookId(UUID bookId);
}
