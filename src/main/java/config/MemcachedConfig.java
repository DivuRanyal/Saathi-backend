package config;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.Collections;

@Configuration
@EnableCaching  // Enables Spring's caching mechanism
public class MemcachedConfig {

    @Bean
    public MemcachedClient memcachedClient() throws Exception {
        // Connect to Memcached server on localhost at port 11211
        return new MemcachedClient(new InetSocketAddress("localhost", 11211));
    }

    @Bean
    public CacheManager cacheManager(MemcachedClient memcachedClient) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(new MemcachedCache("subscriberServicesCache", memcachedClient)));
        return cacheManager;
    }
}
