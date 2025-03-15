package BS.spring_BS.repository;

import BS.spring_BS.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Query("SELECT c FROM CartItem c WHERE c.member.id = :memberId")
    List<CartItem> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT c FROM CartItem c WHERE c.member.id = :memberId AND c.id IN :ids")
    List<CartItem> findByMemberIdAndIdIn(@Param("memberId") Long memberId, @Param("ids") List<Long> ids);

    List<CartItem> findByIdIn(List<Long> ids);
}
