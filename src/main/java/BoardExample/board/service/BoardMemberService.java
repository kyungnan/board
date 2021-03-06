package BoardExample.board.service;

import BoardExample.board.domain.BoardMember;

public interface BoardMemberService {

    int join(BoardMember member);

    void update(BoardMember originMember, BoardMember updateMember);
}
