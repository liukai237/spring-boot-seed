package com.iakuil.bf.common.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类服务接口
 *
 * @author Kai
 */
public interface RedisService {
    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    void expire(String key, long time);

    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param expireAt 失效时间点
     * @return 处理结果
     */
    void expireAt(String key, Date expireAt);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    Boolean hasKey(String key);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    void delete(String... key);

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    void delete(Collection<String> keys);

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    void set(String key, Object value, long time);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    void set(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * 递增
     *
     * @param key   键
     * @param value 要增加几(大于0)
     * @return 递增后结果
     */
    Long incr(String key, long value);

    /**
     * 递减
     *
     * @param key   键
     * @param value 要减少几(大于0)
     * @return 递减后结果
     */
    Long decr(String key, long value);

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    Object hashGet(String key, String item);

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    Map<Object, Object> hashEntries(String key);

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    void hashSet(String key, Map<String, Object> map);

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    void hashSet(String key, Map<String, Object> map, long time);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     */
    void hashSet(String key, String item, Object value);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    void hashSet(String key, String item, Object value, long time);

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    void hashDelete(String key, Object... item);

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param items 项 可以使多个 不能为null
     */
    void hashDelete(String key, Collection items);

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    Boolean hashHasKey(String key, String item);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key   键
     * @param item  项
     * @param value 要增加几(大于0)
     * @return 递增后结果
     */
    Double hashIncr(String key, String item, double value);

    /**
     * hash递减
     *
     * @param key   键
     * @param item  项
     * @param value 要减少记(小于0)
     * @return 递减后结果
     */
    Double hashDecr(String key, String item, double value);

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return set集合
     */
    Set<Object> setGet(String key);

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    Boolean setIsMember(String key, Object value);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    Long setAdd(String key, Object... values);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    Long setAdd(String key, Collection values);

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    Long setAdd(String key, long time, Object... values);

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set长度
     */
    Long setSize(String key);

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    Long setRemove(String key, Object... values);

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return 缓存列表
     */
    List<Object> listRange(String key, long start, long end);

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    Long listSize(String key);

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    Object listIndex(String key, long index);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    void listRightPush(String key, Object value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    void listRightPush(String key, Object value, long time);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    void listRightPushAll(String key, List<Object> value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    void listRightPushAll(String key, List<Object> value, long time);

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    void listSet(String key, long index, Object value);

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    Long listRemove(String key, long count, Object value);
}
