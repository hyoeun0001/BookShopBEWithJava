package BS.spring_BS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BooksResponseDto {
    private Long id;
    private String title;
    private String img;
    private String summary;
    private String author;
    private Integer price;
    private LocalDate pubDate;
    private String form;
    private String isbn;
    private String detail;
    private Integer pages;
    private String contents;
    private Integer categoryId;
    private Integer likes;
}
