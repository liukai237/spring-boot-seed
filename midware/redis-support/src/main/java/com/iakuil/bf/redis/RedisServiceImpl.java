package com.iakuil.bf.redis;

import com.iakuil.bf.common.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类实现
 *
 * @author Kai
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final String PREFIX = "com.iakuil.bf:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    @Override
    public void expire(String key, long time) {
        redisTemplate.expire(getKey(key), time, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param expireAt 失效时间点
     * @return 处理结果
     */
    @Override
    public void expireAt(String key, Date expireAt) {
        redisTemplate.expireAt(getKey(key), expireAt);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(getKey(key), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(getKey(key));
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    @Override
    public void delete(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(getKey(keys[0]));
            } else {
                List<String> keyList = new ArrayList<>(keys.length);
                for (String key : keys) {
                    keyList.add(getKey(key));
                }
                redisTemplate.delete(keyList);
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    @Override
    public void delete(Collection<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            List<String> keyList = new ArrayList<>(keys.size());
            for (String key : keys) {
                keyList.add(getKey(key));
            }
            redisTemplate.delete(keyList);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(getKey(key));
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(getKey(key), value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    @Override
    public void set(String key, Object value, long time) {
        set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间 time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit 时间单位
     */
    @Override
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(getKey(key), value, time, timeUnit);
        } else {
            set(getKey(key), value);
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param value 要增加几(大于0)
     * @return 递增后结果
     */
    @Override
    public Long incr(String key, long value) {
        if (value < 1) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(getKey(key), value);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param value 要减少几(大于0)
     * @return 递减后结果
     */
    @Override
    public Long decr(String key, long value) {
        if (value < 1) {
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(getKey(key), value);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    @Override
    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(getKey(key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    @Override
    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(getKey(key));
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    @Override
    public void hashSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(getKey(key), map);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    @Override
    public void hashSet(String key, Map<String, Object> map, long time) {
        String k = getKey(key);
        redisTemplate.opsForHash().putAll(k, map);
        if (time > 0) {
            expire(k, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     */
    @Override
    public void hashSet(String key, String item, Object value) {
        redisTemplate.opsForHash().putIfAbsent(getKey(key), item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    @Override
    public void hashSet(String key, String item, Object value, long time) {
        String k = getKey(key);
        redisTemplate.opsForHash().putIfAbsent(k, item, value);
        if (time > 0) {
            expire(k, time);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    @Override
    public void hashDelete(String key, Object... item) {
        redisTemplate.opsForHash().delete(getKey(key), item);
    }

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param items 项 可以使多个 不能为null
     */
    @Override
    public void hashDelete(String key, Collection items) {
        redisTemplate.opsForHash().delete(getKey(key), items.toArray());
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    @Override
    public Boolean hashHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(getKey(key), item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key   键
     * @param item  项
     * @param value 要增加几(大于0)
     * @return 递增后结果
     */
    @Override
    public Double hashIncr(String key, String item, double value) {
        if (value < 1) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(getKey(key), item, value);
    }

    /**
     * hash递减
     *
     * @param key   键
     * @param item  项
     * @param value 要减少记(小于0)
     * @return 递减后结果
     */
    @Override
    public Double hashDecr(String key, String item, double value) {
        if (value < 1) {
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(getKey(key), item, -value);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return set集合
     */
    @Override
    public Set<Object> setGet(String key) {
        return redisTemplate.opsForSet().members(getKey(key));
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    @Override
    public Boolean setIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(getKey(key), value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public Long setAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(getKey(key), values);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public Long setAdd(String key, Collection values) {
        return redisTemplate.opsForSet().add(getKey(key), values.toArray());
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public Long setAdd(String key, long time, Object... values) {
        String k = getKey(key);
        Long count = redisTemplate.opsForSet().add(k, values);
        if (time > 0) {
            expire(k, time);
        }
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set长度
     */
    @Override
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(getKey(key));
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @Override
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(getKey(key), values);
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return 缓存列表
     */
    @Override
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(getKey(key), start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    @Override
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(getKey(key));
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    @Override
    public Object listIndex(String key, long index) {
        return redisTemplate.opsForList().index(getKey(key), index);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void listRightPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(getKey(key), value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    @Override
    public void listRightPush(String key, Object value, long time) {
        String k = getKey(key);
        redisTemplate.opsForList().rightPush(k, value);
        if (time > 0) {
            expire(k, time);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void listRightPushAll(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushAll(getKey(key), value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    @Override
    public void listRightPushAll(String key, List<Object> value, long time) {
        String k = getKey(key);
        redisTemplate.opsForList().rightPushAll(k, value);
        if (time > 0) {
            expire(k, time);
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    @Override
    public void listSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(getKey(key), index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(getKey(key), count, value);
    }

    private String getKey(String key) {
        return PREFIX + ":" + key;
    }
}
