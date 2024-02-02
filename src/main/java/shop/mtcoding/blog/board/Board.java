package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data // 여기에는 게터, 세터, 투스트링 있음. @Getter, @Setter, @ToString 이렇게 써도 됨.
@Entity
// 엔티티가 안 적혀 있으면 리플렉션 하지 않아. 적혀있어야 리플렉션 한다. 테이블을 만들어 준다. 파싱도 엔티티가 없으면 되지 않아. 유저레파지토리에       Query query = em.createNativeQuery("select * from user_tb where username=?", User.class);여기의 맨 마지막  User이 부분
@Table(name = "board_tb") // 테이블 이름을 user_tb로 함
public class Board {
    @Id // 얘가 프라이머리키다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오토인크리먼트 설정, 오라클을 쓰려면 아이덴티티 대신에 시퀀스를 써라.
    private int id; // 익명 게시판이라면 스칼라라서 보드 테이블로 쪼개지 않아도 된다고 생각할 수 있지만 유저 옆에 게시글을 붙이면? 익명성이 보장되지 않기 때문에 그러면 안 돼.
    private String title;
    private String content;
    private int userId; // 카멜로 쓰면 데이터에서 자동으로 언더스코어로 바뀜. 포린키. 포린키에 제약조건을 안 넣는 것이 좋다.

    @CreationTimestamp
    private LocalDateTime createdAt;

}
