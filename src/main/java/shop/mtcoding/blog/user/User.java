package shop.mtcoding.blog.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


// 메모리데이터는 개발모드에서만 쓰고 서비스 모드에서는 쓸 수 없다.
@Data // 여기에는 게터, 세터, 투스트링 있음
@Entity // 엔티티가 안 적혀 있으면 리플렉션 하지 않아. 적혀있어야 리플렉션 한다. 테이블을 만들어 준다.
@Table(name = "user_tb") // 테이블 이름을 user_tb로 함
public class User {
    @Id // 얘가 프라이머리키다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오토인크리먼트 설정
    private int id;

    @Column(unique = true) // 중복되는 username 금지
    private String username;

    @Column(length = 60, nullable = false)
    private String password;
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
