package kr.soft.autofeed.media.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.soft.autofeed.domain.Media;

public interface MedioRepository extends JpaRepository<Media, Long>{

}
