package kr.soft.autofeed.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "user_name", length = 30, nullable = false)
    private String userName;

    @Column(name = "email_phone", length = 100, nullable = false)
    private String emailPhone;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "profile_image", length = 255, nullable = false)
    private String profileImage;

    @Column(name = "del_check")
    private Boolean delCheck;

    // 자기 참조(soft delete 시 누가 삭제했는지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "del_user_idx")
    private User delUser;

    @Column(name = "private_check")
    private Boolean privateCheck;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "alert1", length = 255)
    private String alert1;

    @Column(name = "alert2", length = 255)
    private String alert2;

    @Column(name = "alert3", length = 255)
    private String alert3;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.profileImage == null) {
            this.profileImage = "http://blog.naver.com/yomyi00/222556494236";
        }
        if (this.delCheck == null) {
            this.delCheck = false;
        }
        if (this.privateCheck == null) {
            this.privateCheck = false;
        }
    }
}
