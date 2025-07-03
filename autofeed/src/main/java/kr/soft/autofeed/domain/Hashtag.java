package kr.soft.autofeed.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hashtag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_idx")
    private Long hashtagIdx;

    @Column(name = "hashtag_name", nullable = false, unique = true, length = 50)
    private String hashtagName;

    @Column(name = "hashtag_at")
    private LocalDateTime hashtagAt;

    @PrePersist
    protected void onCreate() {
        this.hashtagAt = LocalDateTime.now();
    }

}
