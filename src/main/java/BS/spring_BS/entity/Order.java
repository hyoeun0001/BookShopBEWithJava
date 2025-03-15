package BS.spring_BS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "member_id")
    private Long memberId;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
