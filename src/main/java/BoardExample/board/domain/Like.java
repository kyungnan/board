package BoardExample.board.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Like {
    private int like_id;
    private int like_postno;
    private int like_member;
    private Timestamp reg_date;
    private int like_check;
}
