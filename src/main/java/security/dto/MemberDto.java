package security.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import security.domain.MemberEntity;
import security.domain.Role;
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
        private int mno;
        private String mid;
        private String mpassword;

        public MemberEntity toentity(){

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //비밀번호 암호화 방식

                return MemberEntity.builder()
                        .mid(this.mid)
                        .mpassword(encoder.encode(this.mpassword)) //암호화 된 비밀번호
                        .role(Role.Member) //Role에서 지정한 Member로 저장
                        .build();
        }
}
