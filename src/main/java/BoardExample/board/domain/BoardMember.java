package BoardExample.board.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class BoardMember {
    private int id;                 //회원번호

    @NotBlank(message = "ID는 필수 입력값 입니다.")
    @Length(min = 5, max = 20, message = "ID는 5자 이상 20자 이하로 입력해주세요.")
    private String username;        //아이디

    @NotBlank(message = "PASSWORD는 필수 입력값 입니다.")
    @Length(min = 8, max = 20, message = "PASSWORD는 8자 이상 20자 이하로 입력해주세요.")
    private String password;        //패스워드

    @NotBlank(message = "Name은 필수 입력값 입니다.")
    private String name;            //사용자이름

    @NotBlank(message = "Email은 필수 입력값 입니다.")
    @Email(message = "Email 형식이 아닙니다.")
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
