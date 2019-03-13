package com.cx.finance.biz.util;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.cx.finance.biz.third.AbstractThird;

/**
 * 
 * @类描述：缓存服务
 * @author 陈金虎 2017年1月18日 上午12:50:47
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component("bizCacheUtil")
public class BizCacheUtil extends AbstractThird {
	protected static Logger logger = LoggerFactory.getLogger(BizCacheUtil.class);

	public static boolean BIZ_CACHE_SWITCH = true;// 业务缓存开关，true：打开（即使用缓存）
													// false：关闭（即不使用缓存）

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource(name = "redisIntegerTemplate")
	private RedisTemplate<String, Integer> redisIntegerTemplate;
	
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Object> valueOps;
	
	@Resource(name = "redisStringTemplate")
	private HashOperations<String, String, String> hashOps;
	@Resource(name = "redisStringTemplate")
	private ListOperations<String, String> listOps;

	/**
	 * 执行jedis的incr命令
	 * 
	 * **/
	public long incr(final String key){
		try {
			Long r = redisIntegerTemplate.opsForValue().increment(key, 1);
			return r;
		} catch (Exception e) {
			logger.error("incr", e);
		}
		return 0l;
	}
	/**
	 * 自增命令
	 * @param key 键
	 * @param expiredSeconds 失效时间
	 * @return 自增值
	 */
	public long incr(String key, long expiredSeconds){
		try {
			Long r = redisIntegerTemplate.opsForValue().increment(key, 1);
			if(r<2){
				redisIntegerTemplate.expire(key,expiredSeconds,TimeUnit.SECONDS);
			}
			return r;
		} catch (Exception e) {
			logger.error("incr", e);
		}
		return 0l;
	}
	/**
	 * 自增命令
	 * @param key 键
	 * @param date 失效时间
	 * @return 自增值
	 */
	public long incr(String key, Long delta, Date date){
		try {
			Long r = redisIntegerTemplate.opsForValue().increment(key, delta);
			long curTimeout = redisIntegerTemplate.getExpire(key);
			
			if(curTimeout == -1) { // -2不存在, -1永久
				redisIntegerTemplate.expireAt(key, date);
			}
			return r;
		} catch (Exception e) {
			logger.error("incr", e);
		}
		return 0l;
	}
	
	public void set(String key, String obj, long expiredSeconds) {
		long curTimeout = redisTemplate.getExpire(key);
		valueOps.set(key, obj);
		if(curTimeout == -1) { // -2不存在, -1永久
			redisTemplate.expire(key, expiredSeconds, TimeUnit.SECONDS);
		}
	}
	
	public String get(String key) {
		return (String)valueOps.get(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 *            需要删除缓存的id
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void delCache(final String key) {
		if (!BIZ_CACHE_SWITCH) {
			return;
		}
		try {
			redisTemplate.execute(new RedisCallback() {
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.del(redisTemplate.getStringSerializer().serialize(key));
				}
			});
		} catch (Exception e) {
			logger.error("delCache error", e);

		}
	}

	/**
	 * 查询keys,正则表达式
	 * @return 
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
	
	/**
	 * 批量删除Keys
	 * @return 
	 */
	public void del(Collection<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * Hash 操作
	 * @param value 单位s
	 */
	public void hset(String key, String hkey, String value) {
		hashOps.put(key, hkey, value);
	}
	public void hset(String key, String hkey, String value, long timeoutInSecs) {
		long curTimeout = redisTemplate.getExpire(key);
		hashOps.put(key, hkey, value);
		if(curTimeout == -1) { // -2不存在, -1永久
			redisTemplate.expire(key, timeoutInSecs, TimeUnit.SECONDS);
		}
	}
	public void hset(String key, String hkey, String value, Date date) {
		long curTimeout = redisTemplate.getExpire(key);
		hashOps.put(key, hkey, value);
		if(curTimeout == -1) { // -2不存在, -1永久
			redisTemplate.expireAt(key, date);
		}
	}
	public void hdel(String key, String hkey) {
		try {
			hashOps.delete(key, hkey);
		} catch (Exception e) {
			logger.error("hdel" + key, e);
		}
	}
	public String hget(String key, String hkey) {
		try {
			return hashOps.get(key, hkey);
		} catch (Exception e) {
			logger.error("hget" + key, e);
			return null;
		}
	}
	public void hincrBy(String key, String hkey, Long delta) {
		try {
			hashOps.increment(key, hkey, delta);
		} catch (Exception e) {
			logger.error("hincr" + key, e);
		}
	}
	
	
	/**
	 * List 操作
	 */
	public void lpush(String key, Collection<String> values) {
		listOps.leftPushAll(key, values);
	}
	public Object rpop(String key) {
		return listOps.rightPop(key);
	}
	public long llen(String key) {
		return listOps.size(key);
	}

	public void setTime(String key, String obj, long expiredSeconds) {
		valueOps.set(key, obj);
		redisTemplate.expire(key, expiredSeconds, TimeUnit.SECONDS);
	}


	/**
	 * 锁住某个key值，需要解锁时删除即可
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean getLock(final String key, final String value) {
		if(!BIZ_CACHE_SWITCH){
			return false;
		}
		try{
			return (Boolean)redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.setNX(redisTemplate.getStringSerializer().serialize(key), value.getBytes());
				}
			});
		}catch(Exception e){
			logger.error("getLock error",e);
			return false;
		}
	}
	
}
