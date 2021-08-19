package BoardExample.board.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Reply {
    private int id_reply;
    private String content_reply;
    private int postno;
    private int id_member;
    private Timestamp reg_date;
    private String name;
}
