package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * 컨트롤러의 책임
 * 1. 요청받기(URL - URI 포함)
 * 2. http body(데이터)는 어떻게? (DTO)
 * 3. 기본 mime전략 : x-www.form-urlencoded(username=ssar&password=1234)
 * 4. 유효성 검사하기(body 데이터가 있다면, 없으면 안 함)
 * 5. 클라이언트가 view만 원하는지? 혹은 DB처리 후 view도 원하는지? 여기서 말하는 view는 머스태치 파일
 * 6. view만 원하면 view를 응답하면 끝
 * 7. DB처리를 원하면 Model(DAO)에게 위임 후 view를 응답하면 끝
 */
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository; // 이렇게 해서 의존성 주입을 받을 수 있게 한다.
    private final HttpSession session;

//    public UserController(UserRepository userRepository, HttpSession session) { // 이걸 만들어서 디폴트 생성자를 없애버려.
//        this.userRepository = userRepository;
//        this.session = session;
//    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) { // 원래 로그인은 조회라 겟요청해야하는데 이건 예외야.
        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2. 모델 연결 select * from user_tb where username=? and password=?
        User user = userRepository.findByUsernameAndPassword(requestDTO); // 안전한 정보 requestDTO 넘김
        System.out.println(user);

        if (user == null) {
            return "error/401";
        } else {
            session.setAttribute("sessionUser", user); // 셋어트리뷰트: 해시맵, 찾을 때는 키로 찾아야 함.
            // 3. 응답
            return "redirect:/";
        }
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) { // UserRequest 파일에 있는데  static으로 띄워져 있기 때문에 이렇게 부를 수 있어. 나중에 성별을 받고 싶으면 여기를 수정하지 않고 UserRequest 파일의 클래스에 추가해.
        System.out.println(requestDTO); // DTO란 "Data Transfer Object"의 약자로, 데이터 전송 객체를 의미한다. 클라이언트로부터 전달받은 데이터 오브젝트. 디티오는 레이어가 아니다.
        // 조인을 하려면 데이터 삽입이 필요하니 MVC패턴. 그냥 요청만 보여줄 때는 CV패턴

        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2. DB 인서트 -> Model(유저레파지토리)에게 위임하기
        userRepository.save(requestDTO);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm") // 로그인폼은 뷰만 원한다.
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
