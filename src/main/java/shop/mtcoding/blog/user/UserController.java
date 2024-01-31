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
        // System.out.println(requestDTO); // DTO란 "Data Transfer Object"의 약자로, 데이터 전송 객체를 의미한다. 클라이언트로부터 전달받은 데이터 오브젝트. 디티오는 레이어가 아니다.
        // 조인을 하려면 데이터 삽입이 필요하니 MVC패턴. 그냥 요청만 보여줄 때는 CV패턴

        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2, 3을 하나의 트렌젝션으로 묶는 것이 좋다.
        // 2. 동일 username 체크
        User user = userRepository.findByUsername(requestDTO.getUsername());
        if (user == null) {
            // 3. Model에게 위임하기

            userRepository.save(requestDTO);
        } else {
            return "error/400";
        }


        // 3. DB 인서트 -> Model(유저레파지토리)에게 위임하기
        // 트라이캐치문을 쓰는 것보다 오류를 아예 처음부터 잡는 것이 좋은 이유! IO를 줄일 수 있다. 통신이 일어나서 하드디스크까지 가야하면 IO가 일어나는데 IO를 최대한 줄이는 것이 좋다. IO는 조회를 하기 위해서도 일어나지만 최대한 안 터뜨리는 것이 좋다.
        // 메서드는 실행시에 메모리에 뜨는데 조인 메서드를 세 명이 동시에 때리면 각각 하나씩의 스택이 생긴다.
        // 리드는 상관 없지만 세 명이 동시에 id:cos를 인서트하면 터진다. 그래서 그걸 막기 위해서 락을 건다. isolation이 걸리면서 격리를 시켜버림. 그러지 않으면 데이터의 정확성(무결성)이 깨질 수 있다. 리드 제외, 검증을 해서 인서트, 딜리트, 업데이트를 최소화하는 것이 좋다.
        // 트렌젝션은 변형할 때 걸린다. 누가 인서트를 하는 동안 트렌젝션이 걸려서 다른 애들은 그걸 할 수 없다. 스프링에서는 트렌젝션을 길게 끌 수 있는 기술이 있다. 왜 그렇게 하냐? 트렌젝션을 왜 길게 끌까? 내가 롸이트를 여러번 해야 하는 경우도 있어서!! 이체!!!
        // 기억을 하고 있다가 undo의 데이터로 덮어 씌워야 한다. 트랜젝션에서의 원자성. 될거면 다 되고 아니면 아예 다 되돌린다.
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
        session.invalidate(); // 이게 로그아웃임. 세션에서 해당 서랍 자체를 날려버림. 클라이언트는 가지고 있음.
//        session.removeAttribute("sessionUser"); // 이렇게 하면 세션만 날아감.
        // 클라이언트가 제이세션아이디를 가지고 있어. 그런데 클라이언트가 쿠키 저장소에서 그걸 버렸어. 그래서 은행원한테 갔어. 제이세션아이디 말해봐. 아 없는데? 그럼 넌 새로운 제이세션아이디를 받아. 세션 저장소 유효기간: 30분.
        // 우리는 yml파일에
        return "redirect:/";
    }
}
