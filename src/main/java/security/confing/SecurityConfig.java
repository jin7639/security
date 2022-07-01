package security.confing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import security.service.IndexService;

@Configuration //설정파일을 만들기 위한 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        모든 웹페이지에는 http로부터 요청과 응답이 있음
        http
            .authorizeHttpRequests()
            .antMatchers("/")
            .permitAll()// 모든 접근 허용

            .and()
            .formLogin()// 로그인 페이지 보안 설정
            .loginPage("/member/login")//아이디, 비밀번호를 입력받을 url
            .loginProcessingUrl("/member/logincontroller")// 로그인처리 (아이디와 비밀번호를 입력할 url)
            .usernameParameter("mid")//로그인시 아이디로 입력받을 변수명
            .passwordParameter("mpassword") //로그인시 비밀번호로 받을 변수명
            .failureUrl("/member/login/error")//로그인 실패시 이동할 url

            .and()
            .logout()// 로그아웃 설정
            .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))//로그아웃 실행할 url
            .logoutSuccessUrl("/")//성공시 이동할 url
            .invalidateHttpSession(true) // 세션 초기화

            .and()
            .csrf()//서버에게 요청할 수 있는 페이지
            .ignoringAntMatchers("/member/logincontroller")//로그인 처리
            .ignoringAntMatchers("/member/signupcontroller")//회원가입 처리

            .and()
            .oauth2Login() //oauth2 관련 설정
            .userInfoEndpoint()//유저 정보가 들어오는 위치
            .userService(indexService); //해당 서비스 클래스로 유저 정보 받기
    }

    @Autowired
    private IndexService indexService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //인증 관리 메소드
//        super.configure(auth); // 원래 있던거 안쓰고
        auth.userDetailsService( indexService ).passwordEncoder( new BCryptPasswordEncoder() ); //새로 만들어서 쓴다는 뜻
            //인증 indexService에서 처리              //비밀번호는  BCryptPasswordEncoder()사용

    }
}
