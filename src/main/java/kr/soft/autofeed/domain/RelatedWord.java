package kr.soft.autofeed.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "related_word")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "related_word_idx")
    private Long relatedWordIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_idx", nullable = false)
    private Hashtag hashtag;

    @Column(name = "word", nullable = false, length = 50)
    private String word;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
