package BS.spring_BS.dto;

import lombok.Data;

@Data
public class CartItemRequestDto {

    private Long bookId;
    private Integer quantity;
}
