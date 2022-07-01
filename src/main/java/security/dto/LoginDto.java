package security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoginDto implements UserDetails {
    private String mid;
    private String mpassword;
    private Set<GrantedAuthority> authorities; // 인증(UserDetails 필수)

    public LoginDto(String mid, String mpassword,Collection<? extends GrantedAuthority> authorities) {
                                                    // 계정이 가지고 있는 권한 목록
        this.mid = mid;
        this.mpassword = mpassword;
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
                                        //수정 불가한 집합
    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    //비밀번호 반환
    @Override
    public String getPassword() {
        return this.mpassword;
    }
    //아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }
    // 계정 인증 만료 확인
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정이 잠겨있는지 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //계정 비밀번호 만료 여부 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //계정 사용가능한지 확인
    @Override
    public boolean isEnabled() {
        return true;
    } //
}
