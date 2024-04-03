package com.example.cache.service;

import com.example.cache.domain.entity.User;
import com.example.cache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;

    public User getUser(final Long id){
        User cacheUser = userRedisTemplate.opsForValue().get(getFormatted(id));
        if(cacheUser != null){
            return cacheUser;
        }
        User getUser = userRepository.findById(id).orElseThrow();
        userRedisTemplate.opsForValue().set(getFormatted(id), getUser);
        return getUser;
    }

    private static String getFormatted(Long id) {
        return "usersCache:%d".formatted(id);
    }

}
