package BS.spring_BS.service;

import BS.spring_BS.dto.MemberResponseDto;
import BS.spring_BS.dto.MemberSignInRequestDto;
import BS.spring_BS.dto.MemberSignUpRequestDto;

public interface MemberService {

    public MemberResponseDto getMemberByEmail(String email);

    public String signIn(MemberSignInRequestDto requestDto) throws Exception;

    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception;

    boolean existsByEmail(String email);
    boolean updatePassword(String email, String hashedPassword); // 기존 메서드 유지
    void requestPasswordReset(String email); // 비밀번호 재설정 요청 추가
    void resetPassword(String email, String newPassword);
}
