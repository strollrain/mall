package com.mmall.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author rain
 *
 */
public class TokenCache {
	//日志声明
	private static Logger logger=LoggerFactory.getLogger(TokenCache.class);
	
	public static final String TOKEN_PREFIX="token_";
    //LRU算法
	private static LoadingCache<String, String> localCache =CacheBuilder.newBuilder().initialCapacity(1000).
			maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).
          build(new CacheLoader<String,String>() {
        	  //默认数据加载实现，当调用get取值时，key无对应值 就调用此方法
        	  public String load(String s)throws Exception {
        		  return "null";
        	  }
		});
	public static void setKey(String key,String value) {
		localCache.put(key, value);
	}
	public static String getKey(String key) {
		String value = null;
		try {
			value=localCache.get(key);
			if("null".equals(value)) {
				return null;
			}
			return value;
		}catch(Exception e) {
			logger.error("localCache get error",e);	
		}
		return null;
	}
}
