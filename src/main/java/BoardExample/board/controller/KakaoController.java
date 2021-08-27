package BoardExample.board.controller;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.config.auth.PrincipalDetailsService;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    private final KakaoService kakaoService;
    private final PrincipalDetailsService principalDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/auth/kakao/callback")
    public String KakaoCallback(String code, @AuthenticationPrincipal PrincipalDetails principalDetails){       //Data 리턴해주는 컨트롤러 함수

        // 액세스토큰 가져오기
        String access_token = kakaoService.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_token);
        System.out.println("login Controller : " + userInfo.get("id")+" " + userInfo.get("email"));


        //강제로 회원가입
        BoardMember kakaoMember = BoardMember.builder()
                .username(userInfo.get("id") +"_"+userInfo.get("email"))
                .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                .email((String) userInfo.get("email"))
                .build();

        BoardMember originMember = kakaoService.searchMember(kakaoMember.getUsername());

        if(originMember == null){
            log.info("기존회원이 아니므로 자동 회원가입을 진행합니다.");
            kakaoService.join(kakaoMember);
        }

        //수동 로그인 처리
        UserDetails userDetails = principalDetailsService.loadUserByUsername(kakaoMember.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, kakaoMember.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("기존 회원가입된 회원으로 로그인 완료");
        return "redirect:/";
    }
}
