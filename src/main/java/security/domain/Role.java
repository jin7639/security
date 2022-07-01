package security.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
        //enum 열거형 : 서로 연관된 필드들의 집합
    Member("ROLE_MEMBER","회원"),
    SNSMember("ROLE_MEMBER","회원"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key; //final >> ?
    private final String keyword;
}
