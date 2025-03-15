package BS.spring_BS.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Timestamp createdAt;
    private String address;
    private String receiver;
    private String contact;
    private String bookTitle;
    private int totalPrice;
    private int totalQuantity;

    @Builder
    public OrderResponseDTO(Long orderId, Timestamp createdAt, String address, String receiver, String contact,
                            String bookTitle, int totalPrice, int totalQuantity) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.address = address;
        this.receiver = receiver;
        this.contact = contact;
        this.bookTitle = bookTitle;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }
}
