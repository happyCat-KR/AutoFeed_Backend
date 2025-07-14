package kr.soft.autofeed.UserAction.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.UserAction;

@Repository
public interface UserActionRepository extends JpaRepository <UserAction, Long> {
    
    // UserAction 엔티티에 대한 추가적인 쿼리 메소드가 필요하다면 여기에 정의할 수 있습니다.
    // 예: List<UserAction> findByUserId(Long userId);

}
