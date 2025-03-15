package BS.spring_BS.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailResponseDTO {
    private Long bookId;
    private String title;
    private String author;
    private int price;
    private int quantity;

    @Builder
    public OrderDetailResponseDTO(Long bookId, String title, String author, int price, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }
}
