package BS.spring_BS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String img;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String form;

    private String isbn;

    private String summary;

    @Lob
    private String detail;

    private String author;

    private Integer pages;

    @Lob
    private String contents;

    private Integer price;

    private LocalDate pubDate;

    public Book(String title, String img, Category category, String form, String isbn,
                String summary, String detail, String author, Integer pages,
                String contents, Integer price, LocalDate pubDate) {
        this.title = title;
        this.img = img;
        this.category = category;
        this.form = form;
        this.isbn = isbn;
        this.summary = summary;
        this.detail = detail;
        this.author = author;
        this.pages = pages;
        this.contents = contents;
        this.price = price;
        this.pubDate = pubDate;
    }
}
