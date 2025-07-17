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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_idx")
    private Long actionIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_idx", nullable = false)
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_type", nullable = false)
    private ActionType actionType;

    @Column(name = "del_check")
    private Boolean delCheck = false;

    @Column(name = "action_at")
    private LocalDateTime actionAt;

    @PrePersist
    public void onCreate(){
        this.actionAt = LocalDateTime.now();
    }

}
