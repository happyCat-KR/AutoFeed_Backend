package kr.soft.autofeed.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_hashtag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHashtag {

    @EmbeddedId
    private UserHashtagId userHashtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userIdx")
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hashtagIdx")
    @JoinColumn(name = "hashtag_idx")
    private Hashtag hashtag;

    @Column(name = "del_check")
    private boolean delCheck = false;

    @Column(name = "user_hashtag_at")
    private LocalDateTime userHashtagAt;

    @PrePersist
    protected void onCreate(){
        this.userHashtagAt = LocalDateTime.now();
    }
}
