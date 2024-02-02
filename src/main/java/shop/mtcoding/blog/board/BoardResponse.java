package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {
    @AllArgsConstructor
    @Data
    public static class DetailDTO { // 상세보기 데이터 요청 시에 응답 할 데이터??
        // 풀 생성자를 때려야 함.
//         이거 변수 순서도 중요함
        private Integer id; // int라고 해도 들어옴. 그런데 만약 Bigint라고 적혀 있다면 그렇게 적어야 함.
        private String title;
        private String content;
        private Timestamp createdAt;
        private Integer userId;
        private String username;
    }
}
