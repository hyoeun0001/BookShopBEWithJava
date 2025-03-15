package BS.spring_BS.service;

import BS.spring_BS.entity.Book;
import BS.spring_BS.entity.Like;
import BS.spring_BS.entity.Member;
import BS.spring_BS.repository.BookRepository;
import BS.spring_BS.repository.LikeRepository;
import BS.spring_BS.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LikeService(LikeRepository likeRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void addLike(Long bookId, Long memberId) {
        logger.info("Starting addLike - bookId: {}, memberId: {}", bookId, memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        if (likeRepository.existsByMemberIdAndBookId(memberId, bookId)) {
            logger.warn("Duplicate like attempt - memberId: {}, bookId: {}", memberId, bookId);
            throw new IllegalStateException("Already liked this book");
        }

        Like like = new Like(member, book);
        likeRepository.save(like);
        logger.info("Like added successfully - memberId: {}, bookId: {}", memberId, bookId);
    }

    @Transactional
    public void removeLike(Long bookId, Long memberId) {
        logger.info("Starting removeLike - bookId: {}, memberId: {}", bookId, memberId);

        // Member와 Book 존재 여부 확인 (선택적, 필요 시 제거 가능)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        // 좋아요 존재 여부 확인 후 삭제
        if (!likeRepository.existsByMemberIdAndBookId(memberId, bookId)) {
            logger.warn("No like found to remove - memberId: {}, bookId: {}", memberId, bookId);
            throw new IllegalArgumentException("No like found for this book");
        }

        likeRepository.deleteByMemberIdAndBookId(memberId, bookId);
        logger.info("Like removed successfully - memberId: {}, bookId: {}", memberId, bookId);
    }
}