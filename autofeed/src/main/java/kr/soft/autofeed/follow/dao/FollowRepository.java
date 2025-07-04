package kr.soft.autofeed.follow.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.soft.autofeed.domain.Follow;
import kr.soft.autofeed.domain.FollowId;

public interface FollowRepository extends JpaRepository<Follow, FollowId>{

}
