package io.github.hunknownn.example

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class UserCacheService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val USER_CACHE_KEY_PREFIX = "user:"
        private val CACHE_TTL = Duration.ofMinutes(10)
    }

    fun cacheUser(userId: Long, userName: String) {
        val key = getCacheKey(userId)
        redisTemplate.opsForValue().set(key, userName, CACHE_TTL)
    }

    fun getCachedUser(userId: Long): String? {
        val key = getCacheKey(userId)
        return redisTemplate.opsForValue().get(key)
    }

    fun evictUser(userId: Long) {
        val key = getCacheKey(userId)
        redisTemplate.delete(key)
    }

    fun clearAll() {
        val keys = redisTemplate.keys("$USER_CACHE_KEY_PREFIX*")
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }

    private fun getCacheKey(userId: Long): String {
        return "$USER_CACHE_KEY_PREFIX$userId"
    }
}
