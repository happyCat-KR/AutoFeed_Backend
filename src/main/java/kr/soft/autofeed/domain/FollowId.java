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
public class FollowId implements Serializable{

    private Long followerId;
    private Long followingId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(followerId, that.followerId) &&
                Objects.equals(followingId, that.followingId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(followerId, followingId);
    }
}
