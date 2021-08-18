package BoardExample.board.mapper;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BoardMapper {
    //게시글 등록
    void createPost(Board post);

    //게시글 조회
    List<Board> getList();

    // 페이징 처리
    List<Board> getListWithPaging(Criteria criteria);

    // 게시글 총 개수 조회
    int getTotalCnt();

    // 게시글 상세보기
    Board getByPostNo(int postNo);

    // 게시글 조회수
    void updateCount(int postNo);

    // 게시글 수정
    void updatePost(Board post);

    // 게시글 삭제
    void deletePost(int postNo);

    // 검색 기능(작성자)
    List<Board> searchWriter(@Param("writer") String writer, @Param("criteria") Criteria criteria);

    // 검색 결과수
    int searchCnt(String writer);
}
