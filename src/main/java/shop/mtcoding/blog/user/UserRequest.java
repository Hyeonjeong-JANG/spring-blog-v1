package shop.mtcoding.blog.user;

import lombok.Data;

/**
 * 요청 DTO = Data Transfer Object
 */
public class UserRequest { // 요청 받는 데이터 항아리. 유저한테 요청하는 조인데이터, 로그인데이터...
    @Data
    public static class JoinDTO { // static을 붙이면 다른 곳에서 띄우기 편하다. // JoinDTO를 바로 띄우지 않고 UserRequest로 감싸면 관리하기가 편함.
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

//    @Data
//    public static class UserRequestLoginDTO { // 이렇게도 쓰는데 이름이 거지같음.
//        private String username;
//        private String password;
//    }
}
