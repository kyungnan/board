package BoardExample.board.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BoardDetailsVo{
    private String flag; //댓글수정폼 flag
    private String flagReReply; //대댓글 flag
    private int updateIdReply;  //댓글수정 ID
    private int parent_id;
}
