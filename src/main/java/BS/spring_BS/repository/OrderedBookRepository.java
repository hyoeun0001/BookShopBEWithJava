package BS.spring_BS.repository;

import BS.spring_BS.dto.OrderDetailResponseDTO;
import BS.spring_BS.entity.OrderedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderedBookRepository extends JpaRepository<OrderedBook, Long> {
    @Query("SELECT new BS.spring_BS.dto.OrderDetailResponseDTO(" +
            "ob.bookId, b.title, b.author, b.price, ob.quantity) " +
            "FROM OrderedBook ob " +
            "LEFT JOIN Book b ON ob.bookId = b.id " +
            "WHERE ob.orderId = :orderId")
    List<OrderDetailResponseDTO> findOrderDetailsByOrderId(@Param("orderId") Long orderId);
}
