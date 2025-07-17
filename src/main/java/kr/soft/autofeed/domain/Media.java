package kr.soft.autofeed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_idx")
    private Thread thread;

    private String fileUrl;

    private String fileType;

    private boolean delCheck = false;
}
