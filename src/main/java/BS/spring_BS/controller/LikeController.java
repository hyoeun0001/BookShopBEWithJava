package BS.spring_BS.controller;

import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.service.LikeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/{bookId}")
    public ResponseEntity<String> addLike(
            @PathVariable Long bookId,
            HttpServletRequest request) throws Exception {
        Long memberId = extractMemberIdFromCookie(request);
        likeService.addLike(bookId, memberId);
        return ResponseEntity.ok("Like added successfully");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> removeLike(
            @PathVariable Long bookId,
            HttpServletRequest request) {
        Long memberId = extractMemberIdFromCookie(request);
        likeService.removeLike(bookId, memberId);
        return ResponseEntity.ok("Like removed successfully");
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

        return JwtUtil.extractMemberId(token, secretKey);
    }
}
