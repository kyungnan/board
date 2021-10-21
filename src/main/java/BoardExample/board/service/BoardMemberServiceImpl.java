package BoardExample.board.service;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardMemberServiceImpl implements BoardMemberService {

    private final BoardMemberMapper boardMemberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public int join(BoardMember member) {
        BoardMember oldMember = getMemberById(member.getUsername());

        if (oldMember != null){
            return -1;
        } else {
            //비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(member.getPassword());
            member.setPassword(encodedPassword);
            member.setRole("ROLE_USER");
            boardMemberMapper.joinMember(member);
            return 1;
        }
    }

    @Override
    public void update(BoardMember originMember, BoardMember updateMember) {
        originMember.setName(updateMember.getName());
        originMember.setEmail(updateMember.getEmail());
        boardMemberMapper.updateMember(updateMember);
    }

    private BoardMember getMemberById(String userName){
        return boardMemberMapper.getByUsername(userName);
    }

}
