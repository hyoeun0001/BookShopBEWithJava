package BS.spring_BS.repository;

import BS.spring_BS.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b.id, b.title, b.img, b.summary, b.author, b.price, " +
            "b.pub_date, b.form, b.isbn, b.detail, b.pages, b.contents, " +
            "b.category_id, (SELECT COUNT(*) FROM likes l WHERE l.liked_book_id = b.id) AS likes " +
            "FROM books b " +
            "WHERE (:categoryId IS NULL OR b.category_id = :categoryId) " +
            "AND (:news IS NULL OR b.pub_date BETWEEN :oneMonthAgo AND CURRENT_DATE) " +
            "ORDER BY b.id ASC " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Object[]> findBooksRaw(
            @Param("categoryId") Integer categoryId,
            @Param("news") Boolean news,
            @Param("oneMonthAgo") LocalDate oneMonthAgo,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = "SELECT COUNT(*) FROM books b " +
            "WHERE (:categoryId IS NULL OR b.category_id = :categoryId) " +
            "AND (:news IS NULL OR b.pub_date BETWEEN :oneMonthAgo AND CURRENT_DATE)",
            nativeQuery = true)  // ✅ 네이티브 쿼리 적용!
    int countBooks(
            @Param("categoryId") Integer categoryId,
            @Param("news") Boolean news,
            @Param("oneMonthAgo") LocalDate oneMonthAgo
    );

    @Query(value = "SELECT b.id, b.title, b.img, b.summary, b.author, b.price, " +
            "b.pub_date, b.form, b.isbn, b.detail, b.pages, b.contents, " +
            "b.category_id, c.category_name, " +
            "(SELECT COUNT(*) FROM likes l WHERE l.liked_book_id = b.id) AS likes, " +
            "(SELECT EXISTS (SELECT 1 FROM likes l WHERE l.member_id = :memberId AND l.liked_book_id = b.id)) AS liked " +
            "FROM books b " +
            "LEFT JOIN category c ON b.category_id = c.category_id " +
            "WHERE b.id = :bookId", nativeQuery = true)
    Object[] findBookDetail(@Param("bookId") Long bookId, @Param("memberId") Long memberId);

}
