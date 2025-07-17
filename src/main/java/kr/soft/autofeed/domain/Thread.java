package kr.soft.autofeed.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import kr.soft.autofeed.thread.dto.ThreadViewDTO;

@SqlResultSetMapping(
    name = "ThreadViewMapping",
    classes = @ConstructorResult(
        targetClass = ThreadViewDTO.class,
        columns = {
            @ColumnResult(name = "threadIdx", type = Long.class),
            @ColumnResult(name = "profileImage", type = String.class),
            @ColumnResult(name = "hashtagName", type = String.class),
            @ColumnResult(name = "userIdx", type = String.class),
            @ColumnResult(name = "userId", type = String.class),
            @ColumnResult(name = "content", type = String.class),
            @ColumnResult(name = "fileUrls", type = String.class),
            @ColumnResult(name = "likeCount", type = Integer.class),
            @ColumnResult(name = "commentCount", type = Integer.class)
        }
    )
)
@Entity
@Table(name = "thread")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long threadIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean delCheck = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "del_user_idx")
    private User deletedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_idx")
    private Thread parentThread;

    private String alert1;
    private String alert2;
    private String alert3;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
