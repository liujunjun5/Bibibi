package com.Bibibi.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component("redisUtils")
public class RedisUtils<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    public boolean keyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setex(String key, V value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 设置指定键的过期时间
     *
     * @param key   键
     * @param time  过期时间（毫秒），必须大于0。如果time小于等于0，则不会设置过期时间
     * @return true 表示成功，false 表示失败
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取队列中的所有元素
     *
     * @param key 键
     * @return 返回队列中的元素列表
     */
    public List<V> getQueueList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 将元素推入列表的左侧并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将不会设置过期时间
     * @return true成功 false 失败
     */
    public boolean lpush(String key, V value, Long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time != null && time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从列表中移除指定值的元素
     *
     * @param key   键
     * @param value 要移除的值
     * @return 被移除的元素数量
     */
    public long remove(String key, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, 1, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将多个元素推入列表的左侧并设置过期时间
     *
     * @param key    键
     * @param values 要推入的元素列表
     * @param time   时间(秒) time要大于0 如果time小于等于0 将不会设置过期时间
     * @return true成功 false 失败
     */
    public boolean lpushAll(String key, List<V> values, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从列表的右侧弹出一个元素
     *
     * @param key 键
     * @return 弹出的元素，如果列表为空则返回null
     */
    public V rpop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定键的值加1，并返回当前值
     *
     * @param key 键
     * @return 当前值
     */
    public Long increment(String key) {
        Long count = redisTemplate.opsForValue().increment(key, 1);
        return count;
    }

    /**
     * 将指定键的值加1，并在第一次增加时设置过期时间
     *
     * @param key          键
     * @param milliseconds 过期时间（毫秒）
     * @return 当前值
     */
    public Long incrementex(String key, long milliseconds) {
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            // 设置过期时间
            expire(key, milliseconds);
        }
        return count;
    }

    /**
     * 将指定键的值减1，并在值小于等于0时删除该键
     *
     * @param key 键
     * @return 当前值
     */
    public Long decrement(String key) {
        Long count = redisTemplate.opsForValue().increment(key, -1);
        if (count <= 0) {
            redisTemplate.delete(key);
        }
        logger.info("key:{},减少数量{}", key, count);
        return count;
    }

    /**
     * 根据前缀获取所有匹配的键
     *
     * @param keyPrifix 键前缀
     * @return 匹配的键集合
     */
    public Set<String> getByKeyPrefix(String keyPrifix) {
        Set<String> keyList = redisTemplate.keys(keyPrifix + "*");
        return keyList;
    }

    /**
     * 批量获取指定前缀的键值对
     *
     * @param keyPrifix 键前缀
     * @return 键值对映射
     */
    public Map<String, V> getBatch(String keyPrifix) {
        Set<String> keySet = redisTemplate.keys(keyPrifix + "*");
        List<String> keyList = new ArrayList<>(keySet);
        List<V> keyValueList = redisTemplate.opsForValue().multiGet(keyList);
        Map<String, V> resultMap = keyList.stream().collect(Collectors.toMap(key -> key, value -> keyValueList.get(keyList.indexOf(value))));
        return resultMap;
    }

    /**
     * 将元素添加到有序集合并增加其分数
     *
     * @param key 键
     * @param v   值
     */
    public void zaddCount(String key, V v) {
        redisTemplate.opsForZSet().incrementScore(key, v, 1);
    }

    /**
     * 获取有序集合中指定数量的元素
     *
     * @param key   键
     * @param count 返回的元素数量
     * @return 有序集合中的元素列表
     */
    public List<V> getZSetList(String key, Integer count) {
        Set<V> topElements = redisTemplate.opsForZSet().reverseRange(key, 0, count);
        List<V> list = new ArrayList<>(topElements);
        return list;
    }


}
