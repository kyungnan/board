package BoardExample.board.domain;

import lombok.Data;

@Data
public class BoardMember {
    private int id;                 //회원번호
    private String username;        //아이디
    private String password;        //패스워드
    private String name;            //사용자이름
    private String email;           //사용자이메일
}
