package org.xiaowu.behappy.screw;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.constant.CacheConstant;
import org.xiaowu.behappy.screw.entity.User;

/**
 * 测试cache
 * @author xiaowu
 */
@Service
public class TestCacheService {

    @CachePut(value = CacheConstant.USER_CACHE,key = "#user.id")
    public User testCacheUser(User user){
        return user;
    }

    @Cacheable(value = CacheConstant.USER_CACHE,key = "#id")
    public User getCacheUser(Integer id){
        return new User();
    }
}
