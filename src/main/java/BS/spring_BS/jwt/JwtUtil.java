package BS.spring_BS.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String extractEmail(String token, String secretKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();

        return claims.getSubject();  // 이메일이 subject로 저장된 경우
    }

    public static Long extractMemberId(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            // claims에서 memberId 추출
            return claims.get("memberId", Long.class);  // Long 타입으로 memberId 추출
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
        }
    }

    public static String getUserName(String token, String secretKey){
        String res = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userName", String.class);
        System.out.println("res : " + res);
        return res;
    }

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public static String createJwt(String username, String email, Long memberId, String secretKey, Long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("userName", username);  // userName을 claims에 추가
        claims.put("memberId", memberId);  // memberId를 claims에 추가

        return Jwts.builder()
                .setClaims(claims)  // claims에 모든 데이터를 넣음
                .setSubject(email)  // subject는 이메일
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
