package BoardExample.board.service;

import BoardExample.board.domain.Like;
import BoardExample.board.mapper.BoardLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Service
public class BoardLikeServiceImpl implements BoardLikeService{

    private final BoardLikeMapper boardLikeMapper;

    @Override
    public void checkLike(int like_postno, int like_member) {

        //like_postno와 like_member로 해당 like 찾기
        Like findLike = boardLikeMapper.getByPostnoAndMember(like_postno, like_member);
        if (findLike != null){
            //해당 like의 check값 == 0 이면 checkLike() 로 좋아요하기
            if (findLike.getLike_check() == 0){
                findLike.setLike_check(1);
                findLike.setReg_date(new Timestamp(System.currentTimeMillis()));
                boardLikeMapper.checkLike(findLike);
            } else {    //해당 like의 check값 == 1 이면 deleteLike() 로 좋아요 해제하기
                findLike.setLike_check(0);
                findLike.setReg_date(new Timestamp(System.currentTimeMillis()));
                boardLikeMapper.checkLike(findLike);
            }
        } else {
            Like like = new Like();
            like.setLike_member(like_member);
            like.setLike_postno(like_postno);
            like.setReg_date(new Timestamp(System.currentTimeMillis()));
            like.setLike_check(1);
            boardLikeMapper.addLike(like);
        }

    }
}
