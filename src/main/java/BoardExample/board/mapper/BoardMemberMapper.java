package BoardExample.board.mapper;

import BoardExample.board.domain.BoardMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardMemberMapper {

    //id로 회원 조회
    BoardMember getById(String username);

    //id 중복체크
    int idChk(BoardMember member);

    //회원가입
    void joinMember(BoardMember member);
}
