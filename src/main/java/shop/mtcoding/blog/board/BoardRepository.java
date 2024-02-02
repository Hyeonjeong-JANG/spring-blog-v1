package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public Board findById(int id) {
        Query query = em.createQuery("select b from Board b join fetch b.user u where b.id = :id", Board.class);// select * from board;랑 똑같아. :id :이 물음표랑 똑같은 것 // 이 부분에서 가장 중요한 것은 유저를 보드로 받을 수 있다는 것!!!
        // select b from Board b join fetch b.user; 이너조인
        // select b from Board b left join fetch b.user; 아우터조인
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }
}
