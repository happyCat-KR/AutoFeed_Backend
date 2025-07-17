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
@Table(name = "follow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {

    @EmbeddedId
    private FollowId followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
    private User following;

    @Column(name = "del_check")
    private boolean delCheck = false;

    @Column(name = "followed_at")
    private LocalDateTime followedAt;

    @PrePersist
    public void onCreate(){
        this.followedAt = LocalDateTime.now();
    }
}
