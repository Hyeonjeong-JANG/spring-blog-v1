package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository { // 보드 테이블에 접근하는 DAO
    private final EntityManager em;

    public int count() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    public List<Board> findAll(int page) {
        final int COUNT = 3; // 파이널로 하지 않아야 실수하지 않아. 한 페이지에 몇 개가 나오냐.
        int value = page * COUNT; // 페이지네이션
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?, ?", Board.class); // 페이지네이션 만들 때는 쿼리 스트링을 쓴다. ?들어가는 것. // Board.class 얘는 엔티티라서 받을 수 있는 것이다.
        query.setParameter(1, value);
        query.setParameter(2, COUNT);
        List<Board> boardList = query.getResultList(); // 한 건이어도 리절트리스트로 받아라. 그리고 한 건일 때는 다운캐스팅
        return boardList;
    }

    public BoardResponse.DetailDTO findById(@PathVariable int id) {
//        엔티티가 아닌 것은 JPA가 파싱 안 해준다. 왜 엔티티가 아니냐? 조인했으니까. 왜 보드에 못 받지? 보드는 이 결과랑 다르게 생겼으니까.
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.created_at, bt.user_id, ut.username  from board_tb bt inner join user_tb ut  on bt.user_id = ut.id  where bt.id=?;"); // 엔티티가 아닌 것을 받기 위해서 라이브러리 설치:QRM
        query.setParameter(1, id);
        query.getSingleResult(); // 아이디 하나만 찾는거니까 싱글리절트, 그런데 이렇게 쓰면 오브젝트(테이블)를 리턴하기 때문에
        JpaResultMapper rm = new JpaResultMapper();
        BoardResponse.DetailDTO responseDTO = rm.uniqueResult(query, BoardResponse.DetailDTO.class); // 우리가 테이블을 만들 것은 아니니까 임시로 받을 때 이런 테이블을 임시로 만들어서
        return responseDTO;
    }
}

// 과제
// 로그인을 안 하면 수정, 삭제 버튼 안 보이게
// 내가 쓴 글이 아니면 안 보이게
// 머스태치에는 이프문 안에 == 이런거 못 씀. {{sessionUser == user.id}} 이런 거 안 됨
// 컨트롤러에서 다 짜라!!! 트루 폴스를 하나 짜서 가면 끝이다!!!