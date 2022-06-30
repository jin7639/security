package security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import security.dto.MemberDto;
import security.service.IndexService;

@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String main(){
        return "main"; //main.html 로 이동
    }
    @GetMapping("/member/login")
    public String login(){
        return "login"; //login.html 로 이동

    }
    @GetMapping("/member/signup")
    public String signup(){
        return "signup"; //signup.html 로 이동

    }

    @PostMapping("/member/signupcontroller") //singup.html 에 있는 form - action 으로 값 전송받음
    public String signupcontroller(MemberDto memberDto){    //
        indexService.signup(memberDto); //indesService의 signup 메소드 실행
        return "redirect:/"; //main 페이지 반환
    }
}
