package BS.spring_BS.service.impl;

import BS.spring_BS.dto.MemberResponseDto;
import BS.spring_BS.dto.MemberSignInRequestDto;
import BS.spring_BS.dto.MemberSignUpRequestDto;
import BS.spring_BS.entity.Member;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.repository.MemberRepository;
import BS.spring_BS.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        return new MemberResponseDto(member);
    }

    @Override
    @Transactional
    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception {
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(!requestDto.getPassword().equals(requestDto.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.save(requestDto.toEntity());
        member.encodePassword(passwordEncoder);

        member.addUserAuthority();
        return member.getId();
    }


    public String signIn(MemberSignInRequestDto requestDto){

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        return JwtUtil.createJwt(member.getNickname(), member.getEmail(), member.getId(), secretKey, expiredMs);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email); // 누락된 메서드 추가
    }

    @Override
    @Transactional
    public boolean updatePassword(String email, String hashedPassword) {
        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setPassword(hashedPassword);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public void requestPasswordReset(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        // TODO: 이메일로 재설정 링크 또는 토큰 전송 로직 추가
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        String hashedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(hashedPassword);
        memberRepository.save(member);
    }
}
