package kr.soft.autofeed.ActionType.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.ActionType;

@Repository
public interface ActionTypeRepository extends JpaRepository <ActionType, String> {
    // ActionType 엔티티에 대한 추가적인 쿼리 메소드가 필요하다면 여기에 정의할 수 있습니다.
    // 예: Optional<ActionType> findByTypeCode(String typeCode);

}
