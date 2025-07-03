package kr.soft.autofeed.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadHashtagId implements Serializable {

    private Long threadIdx;
    private Long hashtagIdx;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreadHashtagId)) return false;
        ThreadHashtagId that = (ThreadHashtagId) o;
        return Objects.equals(threadIdx, that.threadIdx) &&
               Objects.equals(hashtagIdx, that.hashtagIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threadIdx, hashtagIdx);
    }
}
