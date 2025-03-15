package BS.spring_BS.controller;

import BS.spring_BS.dto.MemberResponseDto;
import BS.spring_BS.dto.MemberSignInRequestDto;
import BS.spring_BS.dto.MemberSignUpRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Value("${jwt.secret}")
    private String secretKey;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 없습니다.");
        }

        String email = JwtUtil.extractEmail(token, secretKey);
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberSignInRequestDto request, HttpServletResponse response) throws Exception {
        String jwt = memberService.signIn(request);

        // JWT를 쿠키에 저장
        Cookie jwtCookie = new Cookie("jwtToken", jwt);
        jwtCookie.setHttpOnly(true); // JavaScript로 접근 불가
        jwtCookie.setPath("/"); // 쿠키 유효 경로
        jwtCookie.setMaxAge(60 * 60); // 1시간 유효
        jwtCookie.setSecure(true); // HTTPS에서만 전송 (운영 환경에서)
        response.addCookie(jwtCookie); // 쿠키 추가

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그인 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public Long join(@Valid @RequestBody MemberSignUpRequestDto request) throws Exception {
        return memberService.signUp(request);
    }

    @PostMapping("/reset/request")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> passwordResetRequest(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        memberService.requestPasswordReset(email);
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        return response;
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> passwordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        memberService.resetPassword(email, password);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Password updated successfully");
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        // JWT 쿠키 삭제 (expires 설정)
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // 쿠키 즉시 만료
        jwtCookie.setSecure(true); // HTTPS 환경에서만 전송

        response.addCookie(jwtCookie); // 쿠키 삭제 적용

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 성공");
        return ResponseEntity.ok(responseBody);
    }
}