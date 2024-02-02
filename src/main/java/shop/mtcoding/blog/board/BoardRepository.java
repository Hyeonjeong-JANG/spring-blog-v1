package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository { // 보드 테이블에 접근하는 DAO
    private final EntityManager em;

    public int count() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public List<Board> findAll(int page) {
        final int COUNT = 3; // 파이널로 하지 않아야 실수하지 않아. 한 페이지에 몇 개가 나오냐.
        int value = page * COUNT; // 페이지네이션
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?, ?", Board.class); // 페이지네이션 만들 때는 쿼리 스트링을 쓴다. ?들어가는 것.
        query.setParameter(1, value);
        query.setParameter(2, COUNT);
        List<Board> boardList = query.getResultList(); // 한 건이어도 리절트리스트로 받아라. 그리고 한 건일 때는 다운캐스팅
        return boardList;
    }
}
