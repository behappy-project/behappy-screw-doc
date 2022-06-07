package org.xiaowu.behappy.screw.common.cache.enums;

/**
 * 缓存名分类
 */
public enum CacheEnum {
 
    /**
     * 用户/角色
     */
    USER(10, 1000L, 600L),
    /**
     * 用户/角色
     */
    ROLE(10, 1000L, 600L),
    /**
     * 推荐
     */
    OTHER(5, 100L, 120L);
 
    private final Integer initialCapacity;
    private final Long maximumSize;
    private final Long expire;

    CacheEnum(Integer initialCapacity, Long maximumSize, Long expire) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expire = expire;
    }
 
    public Long getMaximumSize() {
        return maximumSize;
    }
 
    public Integer getInitialCapacity() {
        return initialCapacity;
    }
 
    public Long getExpire() {
        return expire;
    }
}