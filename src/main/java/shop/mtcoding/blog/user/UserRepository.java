package shop.mtcoding.blog.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository // 이렇게 하면 아이오씨에 뜬다. 뉴가 된다. 내가 뉴하지 않아도 됨.
public class UserRepository { // DAO라고 생각해라.
    private EntityManager em;


    @Transactional // 디비에 인서트하는 쿼리는 위험하기 때문에 @Transactional이 붙지 않은 것은 전송이 안 됨. 셀렉트는 이게 없어도 됨.
    public void save(UserRequest.JoinDTO requestDTO) { // 컨트롤러는 세이브만 호출하면 돼. // 의존성 주입
        // 내가 인서트 코드를 직접 짬
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values(?, ?, ?)");
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());

//      쿼리 전송
        query.executeUpdate();
        System.out.println("UserRepository에 save 메서드 호출됨");
    }

    @Transactional
    public void saveV2(UserRequest.JoinDTO requestDTO) { // 통신을 해서 받은 데이터를 엔티티로 옮김
        // 하이버네이트가 인서트문을 만들어서 클래스를 바로 넣어 버림.
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());

        em.persist(user);
    }

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username =? and password=?", User.class);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());

        User user = (User) query.getSingleResult(); // 리절트셋이 아니라 오브젝트를 리턴함
        return user;
    }
}