package BoardExample.board.mapper;

import BoardExample.board.domain.BoardMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardMemberMapper {

    //ID로 회원 조회
    BoardMember getById(String username);

    //ID 중복체크
    int idChk(BoardMember member);

    //회원가입
    void joinMember(BoardMember member);

    //ID로 회원이름 조회
    String getNameById(String username);
}
