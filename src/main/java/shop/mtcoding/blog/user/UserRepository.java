package shop.mtcoding.blog.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository // 이렇게 하면 아이오씨에 뜬다. 뉴가 된다. 내가 뉴하지 않아도 됨.
public class UserRepository { // DAO라고 생각해라.
    private EntityManager em;

    @Transactional // 디비에 인서트하는 쿼리는 위험하기 때문에 @Transactional이 붙지 않은 것은 전송이 안 됨. 셀렉트는 이게 없어도 됨.
    public void save(UserRequest.JoinDTO requestDTO) { // 컨트롤러는 세이브만 호출하면 돼. // 의존성 주입 // userRepository.save(requestDTO); 요렇게 불렀음.
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

    /**
     * save 메서드와 saveV2 메서드는 모두 사용자 정보를 저장하는 기능을 수행하는 메서드입니다. 그러나 두 메서드는 다른 방식으로 동작합니다.
     * <p>
     * save 메서드:
     * createNativeQuery 메서드를 사용하여 네이티브 SQL 쿼리를 생성합니다.
     * setParameter 메서드를 사용하여 쿼리의 매개변수에 값을 설정합니다.
     * executeUpdate 메서드를 호출하여 쿼리를 실행하고 데이터베이스에 데이터를 삽입합니다.
     * 이 방식은 순수한 SQL 쿼리를 사용하여 데이터베이스에 직접적으로 접근하는 방식입니다.
     * saveV2 메서드:
     * User 객체를 생성하고 요청으로부터 받은 정보를 설정합니다.
     * em.persist(user)를 호출하여 JPA가 제공하는 영속성 컨텍스트를 사용하여 객체를 데이터베이스에 저장합니다.
     * 이 방식은 JPA를 사용하여 객체를 데이터베이스에 매핑하고 관리하는 방식입니다. JPA는 내부적으로 SQL을 생성하고 데이터베이스에 접근하여 데이터를 조작합니다.
     * 두 메서드의 주요 차이는 데이터베이스 접근 방식입니다. save 메서드는 네이티브 SQL 쿼리를 사용하여 직접 데이터베이스에 접근하고 조작합니다. 반면에 saveV2 메서드는 JPA를 사용하여 객체를 데이터베이스에 매핑하고 조작합니다. JPA는 객체와 데이터베이스 간의 매핑을 담당하므로 더 추상화된 방식으로 데이터베이스 조작을 수행할 수 있습니다.
     * <p>
     * 일반적으로는 saveV2와 같이 JPA를 사용하는 방식이 더 객체지향적이고 유지보수가 용이하며, 데이터베이스에 대한 의존성을 낮출 수 있습니다. JPA는 객체와 데이터베이스 간의 매핑을 처리하고 SQL을 자동으로 생성해주기 때문에 개발자는 보다 객체지향적인 코드를 작성할 수 있습니다.
     */

    public UserRepository(EntityManager em) {// 생성자를 만들어줘야 EntityManager를 사용할 수 있다.
        this.em = em;
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        System.out.println("ur" + 1);
        Query query = em.createNativeQuery("select * from user_tb where username =? and password=?", User.class);
        System.out.println("ur" + 2);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        System.out.println("ur" + 3);
        User user = null; // 리절트셋이 아니라 오브젝트를 리턴함, 노리절트 익셉션을 터뜨림.
//        try {
//            user = (User) query.getSingleResult();
//            return user;
//        } catch (Exception e) {
//            return null;
//        }

        // 어디서 터진지 모르겠다? -> 로그로 추적해라. 찾고 지워라.
        user = (User) query.getSingleResult();
        System.out.println("ur" + 4);
        return user;
        // 리절트셋: 데이터베이스 쿼리의 실행 결과로 반환되는 결과 집합
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class);
        query.setParameter(1, username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}