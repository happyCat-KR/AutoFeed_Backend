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
public class ThreadLikeId implements Serializable {

    private Long threadIdx;
    private Long userIdx;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (!(o instanceof ThreadLikeId)) return false;
        ThreadLikeId that = (ThreadLikeId) o;
        return Objects.equals(threadIdx, that.threadIdx) &&
                Objects.equals(userIdx, that.userIdx);
    }

    @Override
    public int hashCode(){
        return Objects.hash(threadIdx, userIdx);
    }
}
