package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.response.AuthorDetail;
import com.example.demo.dto.response.BookDetail;
import com.example.demo.dto.response.BookProfitDetail;
import com.example.demo.dto.response.CategoryDetail;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.entity.enums.CategoryName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BookMapperTest {

  private final BookMapper mapper = new BookMapper();

  @Test
  void toDetail_shouldMapAllFields() {
    Category category = Category.builder().id(UUID.randomUUID()).categoryName("FICTION").build();
    Author author =
        Author.builder().id(UUID.randomUUID()).lastName("Orwell").firstName("George").build();
    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title("1984")
            .genre("Dystopia")
            .publicationDate(LocalDate.of(1949, 6, 8))
            .purchasePrice(BigDecimal.valueOf(5.00))
            .sellingPrice(BigDecimal.valueOf(12.99))
            .currentStock(10)
            .category(category)
            .authors(Set.of(author))
            .build();

    BookDetail detail = mapper.toDetail(book);

    assertNotNull(detail);
    assertEquals(book.getId(), detail.getIdBook());
    assertEquals("1984", detail.getTitle());
    assertEquals("Dystopia", detail.getGenre());
    assertEquals(LocalDate.of(1949, 6, 8), detail.getPublicationDate());
    assertEquals(BigDecimal.valueOf(5.00), detail.getPurchasePrice());
    assertEquals(BigDecimal.valueOf(12.99), detail.getSellingPrice());
    assertEquals(10, detail.getCurrentStock());
    assertNotNull(detail.getCategory());
    assertEquals(category.getId(), detail.getCategory().getIdCategory());
    assertEquals(CategoryName.FICTION, detail.getCategory().getCategoryName());
    assertEquals(1, detail.getAuthors().size());
    assertEquals("Orwell", detail.getAuthors().getFirst().getLastName());
  }

  @Test
  void toDetail_shouldReturnNullForNullBook() {
    assertNull(mapper.toDetail(null));
  }

  @Test
  void toDetail_shouldHandleNullCategory() {
    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title("Test")
            .purchasePrice(BigDecimal.ONE)
            .sellingPrice(BigDecimal.TEN)
            .currentStock(0)
            .category(null)
            .authors(Set.of())
            .build();

    BookDetail detail = mapper.toDetail(book);
    assertNull(detail.getCategory());
  }

  @Test
  void toDetail_shouldHandleNullCategoryName() {
    Category category = Category.builder().id(UUID.randomUUID()).categoryName(null).build();
    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title("Test")
            .purchasePrice(BigDecimal.ONE)
            .sellingPrice(BigDecimal.TEN)
            .currentStock(0)
            .category(category)
            .authors(Set.of())
            .build();

    BookDetail detail = mapper.toDetail(book);
    assertNull(detail.getCategory().getCategoryName());
  }

  @Test
  void toProfitDetail_shouldMapBookAndProfit() {
    Book book =
        Book.builder()
            .id(UUID.randomUUID())
            .title("1984")
            .purchasePrice(BigDecimal.valueOf(5))
            .sellingPrice(BigDecimal.valueOf(15))
            .currentStock(10)
            .category(null)
            .authors(Set.of())
            .build();

    BookProfitDetail profitDetail = mapper.toProfitDetail(book, BigDecimal.valueOf(100));

    assertNotNull(profitDetail);
    assertEquals(book.getId(), profitDetail.getBook().getIdBook());
    assertEquals(BigDecimal.valueOf(100), profitDetail.getTotalProfit());
  }

  @Test
  void toProfitDetail_shouldReturnNullForNullBook() {
    assertNull(mapper.toProfitDetail(null, BigDecimal.TEN));
  }

  @Test
  void toCategoryDetail_shouldMapFields() {
    Category category =
        Category.builder()
            .id(UUID.fromString("a1111111-1111-1111-1111-111111111111"))
            .categoryName("SCIENCE")
            .build();

    CategoryDetail detail = mapper.toCategoryDetail(category);

    assertEquals(category.getId(), detail.getIdCategory());
    assertEquals(CategoryName.SCIENCE, detail.getCategoryName());
  }

  @Test
  void toCategoryDetail_shouldReturnNullForNullCategory() {
    assertNull(mapper.toCategoryDetail(null));
  }

  @Test
  void toAuthorDetails_shouldMapSetToList() {
    Author a1 =
        Author.builder().id(UUID.randomUUID()).lastName("Orwell").firstName("George").build();
    Author a2 =
        Author.builder().id(UUID.randomUUID()).lastName("Huxley").firstName("Aldous").build();

    List<AuthorDetail> result = mapper.toAuthorDetails(Set.of(a1, a2));

    assertEquals(2, result.size());
  }

  @Test
  void toAuthorDetails_shouldReturnEmptyForNull() {
    assertTrue(mapper.toAuthorDetails(null).isEmpty());
  }

  @Test
  void toAuthorDetails_shouldReturnEmptyForEmptySet() {
    assertTrue(mapper.toAuthorDetails(Set.of()).isEmpty());
  }
}
