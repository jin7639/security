package security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.domain.MemberEntity;
import security.domain.MemberRepository;
import security.domain.Role;
import security.dto.LoginDto;
import security.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;
@Service
public class IndexService implements UserDetailsService {
                                        //커스텀 로그인


    @Autowired
    private MemberRepository memberRepository ;

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
}
