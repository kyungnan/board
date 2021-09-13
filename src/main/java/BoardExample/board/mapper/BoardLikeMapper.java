package BoardExample.board.mapper;

import BoardExample.board.domain.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardLikeMapper {

    //좋아요 조회
    Like getByPostnoAndMember(@Param("like_postno") int like_postno, @Param("like_member") int like_member);

    //좋아요 갯수
    Integer LikeCountGetByPostno(@Param("like_postno") int like_postno);

    //좋아요 추가
    void addLike(Like like);

    //좋아요 체크
    void checkLike(Like like);
}
