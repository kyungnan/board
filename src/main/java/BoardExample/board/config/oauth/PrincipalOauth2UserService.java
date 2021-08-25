package BoardExample.board.config.oauth;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BoardMemberMapper boardMemberMapper;

    @Bean
    private BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("## getClientRegistration: " + userRequest.getClientRegistration());
        log.info("## getAccessToken: " + userRequest.getAccessToken().getTokenValue());
        log.info("## getAttribute: " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();        //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        //해당 ID로 회원가입이 되어있는지 확인
        BoardMember member = boardMemberMapper.getById(username);   // google_sub

        if(member == null){
            member = BoardMember.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            boardMemberMapper.joinMember(member);
        } else {
            log.info("로그인한적 있는 회원입니다. 자동 회원가입 되어있습니다.");
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
