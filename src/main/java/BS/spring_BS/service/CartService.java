package BS.spring_BS.service;

import BS.spring_BS.dto.CartItemRequestDto;
import BS.spring_BS.dto.CartItemResponseDto;
import BS.spring_BS.entity.Book;
import BS.spring_BS.entity.CartItem;
import BS.spring_BS.entity.Member;
import BS.spring_BS.repository.BookRepository;
import BS.spring_BS.repository.CartItemRepository;
import BS.spring_BS.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public CartService(CartItemRepository cartItemRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void addCartItem(CartItemRequestDto requestDto, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> {
                    logger.warn("Book not found with id: {}", requestDto.getBookId());
                    return new IllegalArgumentException("Book not found with id: " + requestDto.getBookId());
                });

        if (cartItemRepository.existsByMemberIdAndBookId(memberId, requestDto.getBookId())) {
            logger.warn("Duplicate cart item attempt - memberId: {}, bookId: {}", memberId, requestDto.getBookId());
            throw new IllegalStateException("This book is already in the cart");
        }

        // CartItem 생성 및 저장
        CartItem cartItem = new CartItem(book, requestDto.getQuantity(), member);
        cartItemRepository.save(cartItem);
        logger.info("Cart item added successfully - memberId: {}, bookId: {}", memberId, requestDto.getBookId());
    }

    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getAllCart(Long memberId, List<Long> selected) {
        logger.info("Starting getAllCart - memberId: {}, selected: {}", memberId, selected);

        // Member 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    logger.warn("Member not found with id: {}", memberId);
                    return new IllegalArgumentException("Member not found with id: " + memberId);
                });

        // CartItem 조회
        List<CartItem> cartItems;
        if (selected != null && !selected.isEmpty()) {
            logger.debug("Fetching cart items with selected IDs: {}", selected);
            cartItems = cartItemRepository.findByMemberIdAndIdIn(memberId, selected);
        } else {
            logger.debug("Fetching all cart items for memberId: {}", memberId);
            cartItems = cartItemRepository.findByMemberId(memberId);
        }

        // DTO로 변환
        List<CartItemResponseDto> responseDtos = cartItems.stream()
                .map(cartItem -> {
                    CartItemResponseDto dto = new CartItemResponseDto();
                    dto.setId(cartItem.getId());
                    dto.setBookId(cartItem.getBook().getId());
                    dto.setTitle(cartItem.getBook().getTitle());
                    dto.setSummary(cartItem.getBook().getSummary());
                    dto.setQuantity(cartItem.getQuantity());
                    dto.setPrice(cartItem.getBook().getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        logger.info("Retrieved {} cart items for memberId: {}", responseDtos.size(), memberId);
        return responseDtos;
    }

    @Transactional
    public void removeCartItem(Long cartItemId, Long memberId) {
        logger.info("Starting removeCartItem - cartItemId: {}, memberId: {}", cartItemId, memberId);

        // Member 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    logger.warn("Member not found with id: {}", memberId);
                    return new IllegalArgumentException("Member not found with id: " + memberId);
                });

        // CartItem 존재 여부 확인 후 삭제
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> {
                    logger.warn("Cart item not found with id: {}", cartItemId);
                    return new IllegalArgumentException("Cart item not found with id: " + cartItemId);
                });

        // 삭제 권한 확인 (선택적)
        if (!cartItem.getMember().getId().equals(memberId)) {
            logger.warn("Unauthorized attempt to remove cart item - cartItemId: {}, memberId: {}", cartItemId, memberId);
            throw new IllegalArgumentException("You are not authorized to remove this cart item");
        }

        cartItemRepository.delete(cartItem);
        logger.info("Cart item removed successfully - cartItemId: {}, memberId: {}", cartItemId, memberId);
    }

}
