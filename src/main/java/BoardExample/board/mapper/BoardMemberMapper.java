package BoardExample.board.mapper;

import BoardExample.board.domain.BoardMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardMemberMapper {

    //Username으로 회원 조회
    BoardMember getByUsername(String username);

    //ID 중복체크
    int idChk(BoardMember member);

    //회원가입
    void joinMember(BoardMember member);

    //ID(PK)로 회원 조회
    BoardMember getById(int id);

    //회원정보 수정
    void updateMember(BoardMember member);

}
