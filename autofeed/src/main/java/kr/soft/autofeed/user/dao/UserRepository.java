package kr.soft.autofeed.user.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     // 이메일/전화번호 존재 여부 체크 (count 대신 existsByEmailPhone 메서드 활용)
    boolean existsByEmailPhone(String emailPhone);

    // 사용자 ID 존재 여부 체크
    boolean existsByUserId(String userId);

    // 이메일/전화번호로 비밀번호 조회 (단일 값 반환)
    @Query("SELECT u.password FROM User u WHERE u.emailPhone = :emailPhone")
    String findPasswordByEmailPhone(@Param("emailPhone") String emailPhone);

    

}
