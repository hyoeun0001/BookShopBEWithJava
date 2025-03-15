package BS.spring_BS.repository;

import BS.spring_BS.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.member.id = :memberId AND l.book.id = :bookId")
    void deleteByMemberIdAndBookId(@Param("memberId") Long memberId, @Param("bookId") Long bookId);

}
