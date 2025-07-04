package kr.soft.autofeed.media.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.soft.autofeed.domain.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>{

}
