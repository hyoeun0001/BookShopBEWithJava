package BS.spring_BS.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 쿠키에서 JWT 가져오기
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) { // 쿠키 이름은 "jwtToken"으로 가정
                    token = cookie.getValue();
                    break;
                }
            }
        }

        logger.info("token from cookie: " + token);

        // 토큰이 없거나 유효하지 않은 경우
        if (token == null) {
            logger.error("JWT 토큰이 쿠키에 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인
        if (JwtUtil.isExpired(token, secretKey)) {
            logger.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // username과 memberId 추출
        String userName = JwtUtil.getUserName(token, secretKey);
        Long memberId = JwtUtil.extractMemberId(token, secretKey);
        logger.info("username: " + userName);
        logger.info("memberId: " + memberId);

        // 인증 객체 설정
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}

