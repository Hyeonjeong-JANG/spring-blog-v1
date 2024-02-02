package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;
    private final BoardRepository boardRepository;

    @GetMapping("/api/board/{id}")
    public @ResponseBody Board apiBoard(@PathVariable int id){ // 보드 객체를 바로 리턴함
        return boardRepository.findById(id);
    }

    @GetMapping({"/", "/board"})
    public String index() {
//        User sessionUser = (User) session.getAttribute("sessionUser");
//        if (sessionUser==null){
//            System.out.println("로그인 안 된 상태입니다.");
//        }else {
//            System.out.println("로그인 된 상태입니다.");
//        }
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/1")
    public String detail() {
        return "board/detail";
    }
}
