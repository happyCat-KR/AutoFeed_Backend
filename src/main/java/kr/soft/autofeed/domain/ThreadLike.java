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
@Table(name = "thread_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadLike {

    @EmbeddedId
    private ThreadLikeId threadLikeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("threadIdx")
    @JoinColumn(name = "thread_idx")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userIdx")
    @JoinColumn(name = "user_idx")
    private User user;

    @Column(name = "del_check")
    private boolean delCheck = false;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    @PrePersist
    public void onCreate(){
        this.likedAt = LocalDateTime.now();
    }
}
