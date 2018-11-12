package com.project.spring.cloud.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 基于jedis 2.9的封装，同时提供了一个简便的redis分布式锁
 *
 * @author wencheng
 */
@Slf4j
public class RedisClient {

    public static final int MAX_FIELD_SIZE = 20;

    private RedisConnectionFactory redisConnectionFactory;

    public RedisClient(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public interface RedisAction<T> {
        /**
         * 执行具体的redis命令
         *
         * @param jedis
         * @return 具体命令返回的结果
         */
        T action(Jedis jedis);
    }

    /**
     * 从池中获取实例执行redis命令
     *
     * @param key    指定的key
     * @param action 具体的命令操作
     * @param <T>    返回值得类型
     * @return
     */
    public <T> T execute(String key, RedisAction<T> action) {
        long start = System.currentTimeMillis();
        RedisConnection redisConnection = null;
        try {
            redisConnection = redisConnectionFactory.getConnection();
            return action.action((Jedis) redisConnection.getNativeConnection());
        } catch (Exception e) {
            log.error("error when execute redis command, key={}", key, e);
            throw new RuntimeException(e);
        } finally {
            if (redisConnection != null) {
                redisConnection.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("debug when execute redis command, key={}, cost={}ms", key, System.currentTimeMillis() - start);
            }
        }
    }

    public void set(String key, String value) {
        execute(key, jedis -> jedis.set(key, value));
    }

    /**
     * key不存在才能设置值
     *
     * @param key
     * @param value
     * @return 更新成功返回1，不成功返回0
     */
    public Long setnx(String key, String value) {
        return execute(key, jedis -> jedis.setnx(key, value));
    }

    /**
     * 为指定的key设置value，并指定过期时间
     *
     * @param key
     * @param seconds
     * @param value   过期时间，秒为单位
     * @return 设置成功返回"OK"
     */
    public String setex(String key, int seconds, String value) {
        return execute(key, jedis -> jedis.setex(key, seconds, value));
    }

    /**
     * 为指定的key设置value，并指定过期时间
     *
     * @param key
     * @param seconds
     * @param value   过期时间，秒为单位
     * @return 设置成功返回"OK"
     */
    public String setex(String key, String value, int seconds) {
        return execute(key, jedis -> jedis.setex(key, seconds, value));
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return key不存在时，返回null
     */
    public String get(String key) {
        return execute(key, jedis -> jedis.get(key));
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值
     *
     * @param key
     * @param value
     * @return 返回给定key的旧值。当key没有旧值时，返回null
     */
    public String getSet(String key, String value) {
        return execute(key, jedis -> jedis.getSet(key, value));
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return execute(key, jedis -> jedis.exists(key));
    }

    /**
     * 为key设置过期时间
     *
     * @param key
     * @param seconds 过期时间，单位为秒
     * @return
     */
    public Long expire(String key, int seconds) {
        return execute(key, jedis -> jedis.expire(key, seconds));
    }

    /**
     * 在指定的时刻过期
     *
     * @param key
     * @param unixTime
     * @return 设置过期时间成功，返回 1，如果key不存在或没办法设置生存时间时，返回 0
     */
    public Long expireAt(String key, long unixTime) {
        return execute(key, jedis -> jedis.expireAt(key, unixTime));
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return 当 key 不存在时，返回 -2；当 key 存在但没有设置剩余生存时间时，返回 -1；否则，以秒为单位，返回 key 的剩余生存时间
     */
    public Long ttl(String key) {
        return execute(key, jedis -> jedis.ttl(key));
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public Long del(String key) {
        return execute(key, jedis -> jedis.del(key));
    }

    public Long append(String key, String value) {
        return execute(key, jedis -> jedis.append(key, value));
    }

    public String substr(String key, int start, int end) {
        return execute(key, jedis -> jedis.substr(key, start, end));
    }

    public Long setrange(String key, long offset, String value) {
        return execute(key, jedis -> jedis.setrange(key, offset, value));
    }

    public String getrange(String key, long startOffset, long endOffset) {
        return execute(key, jedis -> jedis.getrange(key, startOffset, endOffset));
    }

    public Long decrBy(String key, long num) {
        return execute(key, jedis -> jedis.decrBy(key, num));
    }

    public Long decr(String key) {
        return execute(key, jedis -> jedis.decr(key));
    }

    public Long incrBy(String key, long num) {
        return execute(key, jedis -> jedis.incrBy(key, num));
    }

    public Long incr(String key) {
        return execute(key, jedis -> jedis.incr(key));
    }

    public Boolean setbit(String key, long offset, String value) {
        return execute(key, jedis -> jedis.setbit(key, offset, value));
    }

    public Boolean setbit(String key, long offset, boolean value) {
        return execute(key, jedis -> jedis.setbit(key, offset, value));
    }

    public Boolean getbit(String key, long offset) {
        return execute(key, jedis -> jedis.getbit(key, offset));
    }

    public Long bitcount(final String key, long start, long end) {
        return execute(key, jedis -> jedis.bitcount(key, start, end));
    }

    public Long bitcount(final String key) {
        return execute(key, jedis -> jedis.bitcount(key));
    }

    public Long bitpos(final String key, final boolean value) {
        return execute(key, jedis -> jedis.bitpos(key, value));
    }

    public Long hset(String key, String field, String value) {
        return execute(key, jedis -> jedis.hset(key, field, value));
    }

    public String hget(String key, String field) {
        return execute(key, jedis -> jedis.hget(key, field));
    }

    public Long hsetnx(String key, String field, String value) {
        return execute(key, jedis -> jedis.hsetnx(key, field, value));
    }

    public String hmset(String key, Map<String, String> hash) {
        if (hash != null && hash.size() > MAX_FIELD_SIZE) {
            log.warn("hmset命令fields个数超过{}个, key={}, length={}", MAX_FIELD_SIZE, key, hash.size());
        }
        return execute(key, jedis -> jedis.hmset(key, hash));
    }

    public List<String> hmget(String key, String... fields) {
        if (fields != null && fields.length > MAX_FIELD_SIZE) {
            log.warn("hmget命令fields个数超过{}个, key={}, length={}", MAX_FIELD_SIZE, key, fields.length);
        }
        return execute(key, jedis -> jedis.hmget(key, fields));
    }

    public Long hincrBy(String key, String field, long value) {
        return execute(key, jedis -> jedis.hincrBy(key, field, value));
    }

    public Boolean hexists(String key, String field) {
        return execute(key, jedis -> jedis.hexists(key, field));
    }

    public Long hdel(String key, String... field) {
        return execute(key, jedis -> jedis.hdel(key, field));
    }

    public Long hlen(String key) {
        return execute(key, jedis -> jedis.hlen(key));
    }

    public Set<String> hkeys(String key) {
        return execute(key, jedis -> jedis.hkeys(key));
    }

    public List<String> hvals(String key) {
        return execute(key, jedis -> jedis.hvals(key));
    }

    public Map<String, String> hgetAll(String key) {
        return execute(key, jedis -> jedis.hgetAll(key));
    }

    public Long rpush(String key, String... string) {
        return execute(key, jedis -> jedis.rpush(key, string));
    }

    public Long lpush(String key, String... string) {
        return execute(key, jedis -> jedis.lpush(key, string));
    }

    public Long llen(String key) {
        return execute(key, jedis -> jedis.llen(key));
    }

    public List<String> lrange(String key, long start, long end) {
        return execute(key, jedis -> jedis.lrange(key, start, end));
    }

    public String ltrim(String key, long start, long end) {
        return execute(key, jedis -> jedis.ltrim(key, start, end));
    }

    public String lindex(String key, long index) {
        return execute(key, jedis -> jedis.lindex(key, index));
    }

    public String lset(String key, long index, String value) {
        return execute(key, jedis -> jedis.lset(key, index, value));
    }

    public Long lrem(String key, long count, String value) {
        return execute(key, jedis -> jedis.lrem(key, count, value));
    }

    public String lpop(String key) {
        return execute(key, jedis -> jedis.lpop(key));
    }

    public String rpop(String key) {
        return execute(key, jedis -> jedis.rpop(key));
    }

    public Long lpushx(String key, String... string) {
        return execute(key, jedis -> jedis.lpushx(key, string));
    }

    public Long rpushx(String key, String... string) {
        return execute(key, jedis -> jedis.rpushx(key, string));
    }

    public List<String> blpop(int timeout, String key) {
        return execute(key, jedis -> jedis.blpop(timeout, key));
    }

    public List<String> brpop(int timeout, String key) {
        return execute(key, jedis -> jedis.brpop(timeout, key));
    }

    public Long sadd(String key, String... member) {
        return execute(key, jedis -> jedis.sadd(key, member));
    }

    public Set<String> smembers(String key) {
        return execute(key, jedis -> jedis.smembers(key));
    }

    public Long srem(String key, String... member) {
        return execute(key, jedis -> jedis.srem(key, member));
    }

    public String spop(String key) {
        return execute(key, jedis -> jedis.spop(key));
    }

    public Set<String> spop(String key, long count) {
        return execute(key, jedis -> jedis.spop(key, count));
    }

    public Long scard(String key) {
        return execute(key, jedis -> jedis.scard(key));
    }

    public Boolean sismember(String key, String member) {
        return execute(key, jedis -> jedis.sismember(key, member));
    }

    public String srandmember(String key) {
        return execute(key, jedis -> jedis.srandmember(key));
    }

    public List<String> srandmember(String key, int count) {
        return execute(key, jedis -> jedis.srandmember(key, count));
    }

    public Long strlen(String key) {
        return execute(key, jedis -> jedis.strlen(key));
    }

    public Long zadd(String key, double score, String member) {
        return execute(key, jedis -> jedis.zadd(key, score, member));
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return execute(key, jedis -> jedis.zadd(key, scoreMembers));
    }

    public Set<String> zrange(String key, long start, long end) {
        return execute(key, jedis -> jedis.zrange(key, start, end));
    }

    public Long zrem(String key, String... member) {
        return execute(key, jedis -> jedis.zrem(key, member));
    }

    public Double zincrby(String key, double score, String member) {
        return execute(key, jedis -> jedis.zincrby(key, score, member));
    }

    public Long zrank(String key, String member) {
        return execute(key, jedis -> jedis.zrank(key, member));
    }

    public Long zrevrank(String key, String member) {
        return execute(key, jedis -> jedis.zrevrank(key, member));
    }

    public Set<String> zrevrange(String key, long start, long end) {
        return execute(key, jedis -> jedis.zrevrange(key, start, end));
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return execute(key, jedis -> jedis.zrangeWithScores(key, start, end));
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return execute(key, jedis -> jedis.zrevrangeWithScores(key, start, end));
    }

    public Long zcard(String key) {
        return execute(key, jedis -> jedis.zcard(key));
    }

    public Double zscore(String key, String member) {
        return execute(key, jedis -> jedis.zscore(key, member));
    }

    public Long zcount(String key, double min, double max) {
        return execute(key, jedis -> jedis.zcount(key, min, max));
    }

    public Long zcount(String key, String min, String max) {
        return execute(key, jedis -> jedis.zcount(key, min, max));
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        return execute(key, jedis -> jedis.zrangeByScore(key, min, max));
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        return execute(key, jedis -> jedis.zrangeByScore(key, min, max));
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return execute(key, jedis -> jedis.zrevrangeByScore(key, max, min));
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScore(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return execute(key, jedis -> jedis.zrevrangeByScore(key, max, min));
    }

    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScore(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScore(key, max, min, offset, count));
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return execute(key, jedis -> jedis.zrangeByScoreWithScores(key, min, max));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return execute(key, jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScore(key, max, min, offset, count));
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return execute(key, jedis -> jedis.zrangeByScoreWithScores(key, min, max));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return execute(key, jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return execute(key, jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return execute(key, jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return execute(key, jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
    }

    public Long zremrangeByRank(String key, long start, long end) {
        return execute(key, jedis -> jedis.zremrangeByRank(key, start, end));
    }

    public Long zremrangeByScore(String key, double start, double end) {
        return execute(key, jedis -> jedis.zremrangeByScore(key, start, end));
    }

    public Long zremrangeByScore(String key, String start, String end) {
        return execute(key, jedis -> jedis.zremrangeByScore(key, start, end));
    }

    public Long zlexcount(final String key, final String min, final String max) {
        return execute(key, jedis -> jedis.zlexcount(key, min, max));
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return execute(key, jedis -> jedis.zrangeByLex(key, min, max));
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return execute(key, jedis -> jedis.zrangeByLex(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        return execute(key, jedis -> jedis.zrevrangeByLex(key, max, min));
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        return execute(key, jedis -> jedis.zrevrangeByLex(key, max, min, offset, count));
    }

    public Long zremrangeByLex(final String key, final String min, final String max) {
        return execute(key, jedis -> jedis.zremrangeByLex(key, min, max));
    }


    public Long pfadd(final String key, final String... elements) {
        return execute(key, jedis -> jedis.pfadd(key, elements));
    }

    public long pfcount(final String key) {
        return execute(key, jedis -> jedis.pfcount(key));
    }

    /**
     * 获取锁，isPresent()为true表明获取到锁，get()则能获取到token，用于解锁
     *
     * @param key    指定锁
     * @param expire 锁的过期时间，单位秒
     * @return 是否锁定及锁定token
     * @see Optional#isPresent()
     * @see Optional#get()
     */
    public Optional<String> tryLock(String key, long expire) {
        String token = String.valueOf(System.nanoTime());
        RedisConnection redisConnection = null;
        try {
            redisConnection = redisConnectionFactory.getConnection();
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            boolean locked = "OK".equals(jedis.set(key, token, "NX", "EX", expire));
            if (locked) {
                return Optional.of(token);
            }

        } catch (Exception e) {
            log.error("error when execute redis command, key={}", key, e);
            throw new RuntimeException(e);
        } finally {
            if (redisConnection != null) {
                redisConnection.close();
            }
        }
        return Optional.empty();
    }

    private static String UNLOCK_SCRIPT = "local token = redis.call('get',KEYS[1]) if token == ARGV[1] then return redis.call('del',KEYS[1]) elseif token == false then return 1 else return 0 end";

    /**
     * 解除指定的锁定
     *
     * @param key   指定锁
     * @param token 锁定的token
     * @return
     */
    public boolean unlock(String key, String token) {
        RedisConnection redisConnection = null;
        try {
            redisConnection = redisConnectionFactory.getConnection();
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Long num = (Long) jedis.eval(UNLOCK_SCRIPT, 1, key, token);
            return num > 0;
        } catch (Exception e) {
            log.error("error when execute redis command, key={}", key, e);
            throw new RuntimeException(e);
        } finally {
            if (redisConnection != null) {
                redisConnection.close();
            }
        }
    }
}
