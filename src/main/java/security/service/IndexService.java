package security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import security.domain.MemberEntity;
import security.domain.MemberRepository;
import security.domain.Role;
import security.dto.LoginDto;
import security.dto.MemberDto;
import security.dto.Oauth2Dto;

import java.util.*;

@Service
public class IndexService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
                                        //커스텀 로그인           //oauth 회원
    @Autowired
    private MemberRepository memberRepository ;

    //소셜 로그인 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        // ??
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        //SNS 구분용 아이디 [네이버 카카오] : oauth 구분용으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //인증 정보 결과 저장
        Map<String, Object> attributes = oAuth2User.getAttributes();
        //회원정보 호출시 사용하는 키(이름)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        //어떤 SNS로 로그인 하는지 확인해서 dto에서 꺼내오기
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationId,attributes,userNameAttributeName);
        //로그인 시도하는 id가 DB에 존재하는지 확인하기
        Optional<MemberEntity> optional =memberRepository.findBymid(oauth2Dto.getMid());
        if(!optional.isPresent()){ // 존재하지 않으면
            memberRepository.save(oauth2Dto.toentity()); // DB에 저장
        }

        return new DefaultOAuth2User( // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
                // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수 ]
                Collections.singleton(new SimpleGrantedAuthority("SNSMember")),
                oAuth2User.getAttributes(),userNameAttributeName);
    }

    //회원가입 처리
    public void signup(MemberDto memberDto){ //memberDto 형식으로 받아서
       memberRepository.save(memberDto.toentity()); // entity로 변환해서 DB에 저장
    }

    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException { //로그인 처리

        MemberEntity memberEntity = memberRepository.findBymid(mid).get(); //
        List<GrantedAuthority> authorities = new ArrayList<>();
            //GrantedAuthority  : 부여된 인증 클래스
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKey()));
            //인증된 엔티티 키를 리스트에 보관
        LoginDto LoginDto = new LoginDto(memberEntity.getMid(),memberEntity.getMpassword() , authorities);
            //memberEntity에서 mid, mpassword ,  authorities 가져오기
        return LoginDto; //logindto 반환
    }

    public String 인증결과호출(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        String mid =null;
        if(principal != null){
            if(principal instanceof UserDetails) {
                mid = ((UserDetails)principal).getUsername();
            }else if(principal instanceof OAuth2User){
                Map<String, Object > attributes = ((OAuth2User)principal).getAttributes();
                if(attributes.get("response") != null) {
                    Map<String, Object> map = (Map <String,Object>)attributes.get("response");
                    mid = map.get("email").toString();
                }else if(attributes.get("kakao_account")!=null){
                    Map<String, Object> map = (Map <String,Object>)attributes.get("response");
                    mid = map.get("email").toString();
                }
            }
            return mid;
        }else{
            return mid;
        }

    }



}
