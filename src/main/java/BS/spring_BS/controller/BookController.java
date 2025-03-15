package BS.spring_BS.controller;

import BS.spring_BS.dto.BookDetailDto;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.service.BookService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Value("${jwt.secret}")
    private String secretKey;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBooks(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Boolean news,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int currentPage) {

        // oneMonthAgo를 여기서 선언해야 함
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        Map<String, Object> response = bookService.getAllBooks(categoryId, news, oneMonthAgo, limit, currentPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailDto> getBookDetail(@PathVariable Long bookId, HttpServletRequest request) {
        try {
            // 쿠키에서 JWT 가져오기
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

            // 토큰이 없는 경우
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            Long memberId = JwtUtil.extractMemberId(token, secretKey);
            BookDetailDto bookDetail = bookService.getBookDetail(bookId, memberId);
            return ResponseEntity.ok(bookDetail);
        } catch (IllegalArgumentException e) {
            System.out.println("no book");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.out.println("request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
