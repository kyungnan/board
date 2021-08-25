package BoardExample.board.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardMember {
    private int id;                 //회원번호
    private String username;        //아이디
    private String password;        //패스워드
    private String name;            //사용자이름
    private String email;           //사용자이메일
    private String role;            //사용자권한

    private String provider;        //google
    private String providerId;      //google_sub

    @Builder
    public BoardMember(String username, String password, String name, String email, String role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
