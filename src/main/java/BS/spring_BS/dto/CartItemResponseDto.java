package BS.spring_BS.dto;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long bookId;
    private String title;
    private String summary;
    private Integer quantity;
    private Integer price;
}
