package back.vybz.auth_user.user.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil<T> {

    private final RedisTemplate<String, T> redisTemplate;

    public void save(String key, T value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Long increase(String key, long timeout, TimeUnit unit) {
        Long value = redisTemplate.opsForValue().increment(key);
        if (value != null && value == 1L) {
            redisTemplate.expire(key, timeout, unit);
        }
        return value;
    }

    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }
}
