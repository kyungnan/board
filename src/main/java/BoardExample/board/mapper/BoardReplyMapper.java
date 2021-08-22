package BoardExample.board.mapper;

import BoardExample.board.domain.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardReplyMapper {
    // 댓글 조회
    List<Reply> getByPostNo(@Param("postno") int postno);

    // 댓글 작성
    void createReply(Reply reply);

    // 댓글 삭제
    void deleteReply(int id_reply);
}
