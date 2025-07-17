package kr.soft.autofeed.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHashtagId implements Serializable {

    private Long userIdx;
    private Long hashtagIdx;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserHashtagId)) return false;
        UserHashtagId that = (UserHashtagId) o;
        return Objects.equals(userIdx, that.userIdx) &&
                Objects.equals(hashtagIdx, that.hashtagIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdx, hashtagIdx);
    }
}
