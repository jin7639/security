package security.dto;

import lombok.*;
import security.domain.MemberEntity;
import security.domain.Role;

import java.util.Map;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Oauth2Dto { //oauth 회원의 정보를 담는 dto

    private String mid; //아이디
    private String registrationId; //sns 구분 아이디
    private Map<String , Object> attributes; //인증결과 (회원정보)
    private String nameAttributekey; //회원정보 요청키 (map의 이름)

    //sns 구분 메소드
    public static Oauth2Dto sns확인(String registrationId, Map<String, Object> attributes, String nameAttributekey){
        if(registrationId.equals("naver")){ //registrationId 가 naver이면
            return 네이버변환(registrationId,attributes,nameAttributekey); //네이버 변환 메소드 실행
        }else if(registrationId.equals("kakao")){ //registrationId 가 kakao이면
            return 카카오변환(registrationId, attributes,nameAttributekey); //카카오변환 메소드 실행
        }else{
            return null; //둘 다 아니면 null 반환
        }
    }

    public static Oauth2Dto 네이버변환(String registrationId, Map<String, Object> attributes, String nameAttributekey){
                                                                // 회원정보 객체                   // 회원정보가 저장된 객체의 이름
        Map<String, Object> response = (Map<String, Object>) attributes.get(nameAttributekey);
        //  회원정보 map (response라고함 -> 네이버가 정한 이름) = nameAttributekey이 일치하는 map (회원정보)를 불러온다
        return Oauth2Dto.builder()
                .mid((String)response.get("email")) //이메일을 아이디로 사용
                .build();
    }
    public static Oauth2Dto 카카오변환(String registrationId, Map<String, Object> attributes, String nameAttributekey){
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributekey);
        return Oauth2Dto.builder()
                .mid((String)kakao_account.get("email")) //이메일을 아이디로 사용
                .build();
    }

    // dto -> entity 변환
    public MemberEntity toentity(){

        return MemberEntity.builder()
                .mid(this.mid)
                .role(Role.SNSMember)  // oauth회원은 role에 SNSMember 저장
                .build();
    }


}
