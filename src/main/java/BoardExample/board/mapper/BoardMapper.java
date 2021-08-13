package BoardExample.board.mapper;

import BoardExample.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardMapper {
    //게시글 등록
    void createPost(Board post);

    //게시글 조회
    List<Board> getList();

    // 게시글 상세보기
    Board getByPostNo(int postNo);
}
