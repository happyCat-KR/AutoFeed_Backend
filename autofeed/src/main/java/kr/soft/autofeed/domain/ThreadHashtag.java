package kr.soft.autofeed.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thread_hashtag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadHashtag {

    @EmbeddedId
    private ThreadHashtagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("threadIdx")
    @JoinColumn(name = "thread_idx")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hashtagIdx")
    @JoinColumn(name = "hashtag_idx")
    private Hashtag hashtag;

    @Column(name = "del_check")
    private boolean delCheck = false;

    @Column(name = "thread_hashtag_at")
    private LocalDateTime threadHashtagAt;

    @PrePersist
    protected void onCreate(){
        this.threadHashtagAt = LocalDateTime.now();
    }
}
