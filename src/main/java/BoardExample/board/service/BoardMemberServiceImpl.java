package BoardExample.board.service;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardMemberServiceImpl implements BoardMemberService{

    private final BoardMemberMapper boardMemberMapper;

    @Override
    public int join(BoardMember member) {
        BoardMember oldMember = getMemberById(member.getUsername());

        if (oldMember != null){
            return -1;
        } else {
            boardMemberMapper.joinMember(member);
            return 1;
        }
    }

    private BoardMember getMemberById(String userName){
        return boardMemberMapper.getById(userName);
    }
}
