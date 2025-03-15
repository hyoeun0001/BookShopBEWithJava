package BS.spring_BS.controller;

import BS.spring_BS.dto.OrderRequestDTO;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.service.OrderService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Value("${jwt.secret}")
    private String secretKey;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDTO request,
            HttpServletRequest httpServletRequest) {
        try {
            Long memberId = extractMemberIdFromCookie(httpServletRequest);
            return orderService.createOrder(request, memberId);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 세션이 만료되었습니다. 다시 로그인 하세요.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("잘못된 토큰입니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("토큰이 필요합니다.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getOrders(HttpServletRequest httpServletRequest) {
        try {
            Long memberId = extractMemberIdFromCookie(httpServletRequest);
            return orderService.getOrders(memberId);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "로그인 세션이 만료되었습니다. 다시 로그인 하세요."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "잘못된 토큰입니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "토큰이 필요합니다."));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(
            @PathVariable("orderId") Long orderId,
            HttpServletRequest httpServletRequest) {
        try {
            Long memberId = extractMemberIdFromCookie(httpServletRequest);
            return orderService.getOrderDetail(orderId);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "로그인 세션이 만료되었습니다. 다시 로그인 하세요."));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "잘못된 토큰입니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "토큰이 필요합니다."));
        }
    }

    // 공통 메서드로 토큰 추출 로직 분리
    private Long extractMemberIdFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) { // 다른 컨트롤러와 동일한 쿠키 이름 사용
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new IllegalArgumentException("JWT 토큰이 쿠키에 없습니다.");
        }

        Long memberId = JwtUtil.extractMemberId(token, secretKey);
        if (memberId == null) {
            throw new JwtException("잘못된 토큰입니다.");
        }

        return memberId;
    }
}