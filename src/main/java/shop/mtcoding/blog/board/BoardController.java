package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog._core.PagingUtil;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;

    // http://localhost:8080?page=0
    @GetMapping({"/", "/board"}) // MVC 패턴
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) { // http://localhost:8080  이렇게 하면 원래 터지는데 자동으로 0을 넣어줌.
        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList", boardList);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        boolean first = PagingUtil.isFirst(currentPage);
        boolean last = PagingUtil.isLast(currentPage, boardRepository.count());

        request.setAttribute("first", first);
        request.setAttribute("last", last);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}") // 보드 뒤에 있는 것은 프라이머리키
//    겟요청은 전부다 웨얼절에 보내려고 보내느 것인데 웨얼절에서도 컬럼을 정할 수 있잖아? 그 때 프라이머리키, 유니크한 애들은 이제부터 쿼리스트링에 달지 말자!! op.gg/summoners/kr/방구끼다성났어-KR1 여기서 방구끼다성났어는 유니크한 아이디이기 때문에 테이블명 뒤에 슬래시 뒤에 바로 단다.
    public String detail(@PathVariable int id, HttpServletRequest request) {
        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id); // 얘를 가방에 담으면 끝! 그 가방은 디스패쳐한테 받음. 그 가방은 또 톰캣한테 받음
        request.setAttribute("board", responseDTO);

        // 1. 해당 페이지의 주인 여부
        boolean owner = false;


        // 2. 작성자 userId 확인하기
        int boardUserId = responseDTO.getUserId();

        // 3. 로그인 여부 체크
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser != null) { // 로그인 했고
            if (boardUserId == sessionUser.getId()) {
                owner = true;
            }
        }

        request.setAttribute("owner", owner);

        return "board/detail"; // forward!! redirect 아님
    }
}
// 스프링의 기본 파싱 전략: x-www.form-urlencoded