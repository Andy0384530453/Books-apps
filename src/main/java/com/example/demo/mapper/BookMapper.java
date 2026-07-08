package com.example.demo.mapper;

import com.example.demo.dto.response.AuthorDetail;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.dto.response.CategoryDetail;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.entity.enums.CategoryName;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  public BookDetail toDetail(Book book) {
    if (book == null) return null;
    return BookDetail.builder()
        .idBook(book.getId())
        .title(book.getTitle())
        .genre(book.getGenre())
        .publicationDate(book.getPublicationDate())
        .purchasePrice(book.getPurchasePrice())
        .sellingPrice(book.getSellingPrice())
        .currentStock(book.getCurrentStock())
        .category(toCategoryDetail(book.getCategory()))
        .authors(toAuthorDetails(book.getAuthors()))
        .build();
  }

  public BookProfitDetail toProfitDetail(Book book, BigDecimal totalProfit) {
    if (book == null) return null;
    return BookProfitDetail.builder().book(toDetail(book)).totalProfit(totalProfit).build();
  }

  public CategoryDetail toCategoryDetail(Category category) {
    if (category == null) return null;
    return CategoryDetail.builder()
        .idCategory(category.getId())
        .categoryName(
            category.getCategoryName() == null
                ? null
                : CategoryName.valueOf(category.getCategoryName()))
        .build();
  }

  public List<AuthorDetail> toAuthorDetails(Set<Author> authors) {
    if (authors == null) return Collections.emptyList();
    return authors.stream()
        .map(
            a ->
                AuthorDetail.builder()
                    .idAuthor(a.getId())
                    .lastName(a.getLastName())
                    .firstName(a.getFirstName())
                    .build())
        .collect(Collectors.toList());
  }
}
