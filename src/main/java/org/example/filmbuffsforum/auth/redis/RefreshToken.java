package org.example.filmbuffsforum.auth.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@RedisHash("refresh-tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @Indexed
    private Integer id;

    @Indexed
    private Integer userId;

    @Indexed
    private String token;

    @Indexed
    private Instant expiryDate;
}
