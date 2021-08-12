package BoardExample.board.mapper;

import BoardExample.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardMapper {
    //게시글 등록
    void createPost(Board post);
}
