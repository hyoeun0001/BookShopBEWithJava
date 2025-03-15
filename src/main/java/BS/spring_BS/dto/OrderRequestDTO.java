package BS.spring_BS.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private List<Long> items; // cart item IDs
    private DeliveryDTO delivery;
    private int totalQuantity;
    private int totalPrice;
    private String firstBookTitle;
}
