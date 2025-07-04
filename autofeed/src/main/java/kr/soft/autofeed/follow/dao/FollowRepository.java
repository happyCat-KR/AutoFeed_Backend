package kr.soft.autofeed.follow.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.Follow;
import kr.soft.autofeed.domain.FollowId;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId>{

}
