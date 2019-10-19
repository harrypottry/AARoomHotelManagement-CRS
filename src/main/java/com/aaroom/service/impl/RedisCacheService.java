package com.aaroom.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisCacheService {

    @Autowired
    private JedisPool jedisPool;

    private int defaultExpire = 3600;

    
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);

        }
    }

    
    public boolean hexists(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hexists(key, field);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);

        }
    }

    
    public long incr(String key, int ex) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long v = jedis.incr(key);
            if (ex > 0)
                jedis.expire(key, ex);
            return v;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return 0;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    /**
     * 删除
     * 
     * @param key
     * @param fields
     * @return
     */
    public boolean hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, fields);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
        return true;
    }

    /**
     * 设置
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, String field, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key.getBytes(), field.getBytes(), JSON.toJSONBytes(value));
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
        return true;
    }

    /**
     * 获取
     * 
     * @param key
     * @param field
     * @param type
     * @return
     */
    public <T> T hget(String key, String field, Class<T> type) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String text = jedis.hget(key, field);
            return JSON.parseObject(text, type);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取某个值
     * 
     * @param key
     * @param field
     * @param type
     * @return
     */
    public <T> T hget(String key, String field, TypeReference<T> type) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String text = jedis.hget(key, field);
            return JSON.parseObject(text, type);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    public Set<String> hkeys(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hkeys(key);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取全部
     * 
     * @param key
     * @return
     */
    public Map<String, String> hgetall(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hgetAll(key);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    public Map<String, String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> l = jedis.hmget(key, fields);
            Map<String, String> map = new HashMap<String, String>(fields.length);
            for (int i = 0, j = fields.length; i < j; i++) {
                map.put(fields[i], l.get(i));
            }
            return map;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return new HashMap<String, String>();
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    // ------------------for hash-------

    // 单个的值
    public boolean put(String key, Object val, int ex) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key.getBytes(), ex, JSON.toJSONBytes(val));
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
        return true;
    }

    public boolean put(String key, Object val) {
        return put(key, val, defaultExpire);
    }

    public <T> T get(String key, Type type) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes == null)
                return null;
            return JSON.parseObject(bytes, type);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    public <S, T> Map<S, T> mget(String prefix, S[] keys, Class<T> type) {
        Jedis jedis = null;
        Map<S, T> ret = new java.util.LinkedHashMap<S, T>(keys.length);
        try {
            jedis = jedisPool.getResource();
            String[] strKeys = new String[keys.length];
            for (int i = 0, j = keys.length; i < j; i++)
                strKeys[i] = prefix + keys[i];

            List<String> strRet = jedis.mget(strKeys);
            for (int i = 0, j = keys.length; i < j; i++) {
                String val = strRet.get(i);
                ret.put(keys[i], val == null ? null : JSON.parseObject(val, type));
            }

        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return new HashMap<S, T>();
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
        return ret;
    }

    public String get(String key) {
        //获取连接
        Jedis jedis = jedisPool.getResource();
        try {
            String result = jedis.get(key);
            // 资源还回到连接池当中
            jedisPool.returnResource(jedis);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //销毁资源
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
    }

    public <T> T get(String key, Class<T> type) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes == null)
                return null;
            return JSON.parseObject(bytes, type);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    public <T> T get(String key, TypeReference<T> type) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String text = jedis.get(key);
            if (text == null)
                return null;
            return JSON.parseObject(text, type);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    public boolean del(String... key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (String k : key)
                jedis.del(k);
            return true;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Set<String> keys(String prefix) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> v = jedis.keys(prefix + '*');
            return v;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Long sadd(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long v = jedis.sadd(key, members);
            return v;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> v = jedis.smembers(key);
            return v;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long v = jedis.scard(key);
            return v;
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Long lpush(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, members);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpop(key);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return null;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.expire(key.getBytes(), seconds);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return 0L;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }

    
    public boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key.getBytes(), member.getBytes());
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return false;
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
    }
}
