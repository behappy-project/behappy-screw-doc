package org.xiaowu.behappy.screw;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.xiaowu.behappy.screw.entity.User;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ScrewApplicationTests {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private TestCacheService testCacheService;

    @SneakyThrows
    @Test
    void contextLoads() {
        User user = new User();
        user.setId(123);
        user.setNickname("小五");
        testCacheService.testCacheUser(user);
        TimeUnit.SECONDS.sleep(1);
        User cacheUser = testCacheService.getCacheUser(user.getId());
        System.out.println(cacheManager.getCacheNames());
        System.out.println(cacheUser);
    }

}
