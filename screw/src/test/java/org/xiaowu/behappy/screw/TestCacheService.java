package org.xiaowu.behappy.screw;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.cache.constants.CacheConstants;
import org.xiaowu.behappy.screw.entity.User;

/**
 * 测试cache 咖啡因
 * @author xiaowu
 */
@Service
public class TestCacheService {

    @CachePut(value = CacheConstants.USER_CACHE,key = "#user.id")
    public User testCacheUser(User user){
        return user;
    }

    @Cacheable(value = CacheConstants.USER_CACHE,key = "#id")
    public User getCacheUser(Integer id){
        return new User();
    }
}
