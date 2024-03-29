package projectus.pus.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@RedisHash("refreshToken")
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public static RefreshToken createRefreshToken(String email, String refreshToken, Long remainingMilliSeconds) {
        return RefreshToken.builder()
                .id(email)
                .refreshToken(refreshToken)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }
}
