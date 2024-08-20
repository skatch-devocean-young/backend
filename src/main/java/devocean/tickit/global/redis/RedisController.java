package devocean.tickit.global.redis;

import devocean.tickit.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RedisController {

    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redisTest")
    public ApiResponse<?> addRedisKey() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("key1", "value1");
        ops.set("key2", "value2");
        ops.set("key3", "value3");
        return ApiResponse.created(null);
    }

    @GetMapping("/redisTest/{key}")
    public ApiResponse<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String value = ops.get(key);
        return ApiResponse.ok(value);
    }
}
