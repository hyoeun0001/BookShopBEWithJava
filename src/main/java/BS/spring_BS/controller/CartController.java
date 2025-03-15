package BS.spring_BS.controller;

import BS.spring_BS.dto.CartItemRequestDto;
import BS.spring_BS.dto.CartItemResponseDto;
import BS.spring_BS.dto.CategoryResponseDTO;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping
    public ResponseEntity<String> addCartItem(
            @RequestBody CartItemRequestDto requestDto,
            HttpServletRequest request) {
        Long memberId = extractMemberIdFromCookie(request);
        cartService.addCartItem(requestDto, memberId);
        return ResponseEntity.ok("Cart item added successfully");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getAllCart(
            HttpServletRequest request,
            @RequestBody(required = false) List<Long> selected) {
        Long memberId = extractMemberIdFromCookie(request);
        List<CartItemResponseDto> cartItems = cartService.getAllCart(memberId, selected);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable Long cartItemId,
            HttpServletRequest request) {
        Long memberId = extractMemberIdFromCookie(request);
        cartService.removeCartItem(cartItemId, memberId);
        return ResponseEntity.ok("Cart item removed successfully");
    }

    // 공통 메서드로 토큰 추출 로직 분리
    private Long extractMemberIdFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) { // MemberController와 동일한 쿠키 이름 사용
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new IllegalArgumentException("JWT 토큰이 쿠키에 없습니다.");
        }

        return JwtUtil.extractMemberId(token, secretKey);
    }
}
