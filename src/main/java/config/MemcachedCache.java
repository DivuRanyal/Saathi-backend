package config;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

public class MemcachedCache implements Cache {

    private final String name;
    private MemcachedClient memcachedClient;
    private static final int EXPIRATION_TIME_SECONDS = 36000000; // 1 hour expiration

    public MemcachedCache(String name, MemcachedClient memcachedClient) {
        this.name = name;
        this.memcachedClient = memcachedClient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return memcachedClient;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = memcachedClient.get(key.toString());
        if (value != null) {
            System.out.println("Cache hit for key: " + key);
            return () -> value;  // Wrap the value in a ValueWrapper
        }
        System.out.println("Cache miss for key: " + key);
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            System.out.println("Skipping caching for key: " + key + " because the value is null.");
            return;
        }

        try {
            // Check if MemcachedClient is connected
            if (memcachedClient.getAvailableServers().isEmpty()) {
                System.out.println("No available Memcached servers. Attempting to reconnect...");
                reconnectToMemcached();
            }

            // Set the cache value with the expiration time
            System.out.println("Storing key: " + key + " in Memcached with value: " + value);
            memcachedClient.set(key.toString(), EXPIRATION_TIME_SECONDS, value);

        } catch (Exception e) {
            System.out.println("Failed to store key: " + key + " in Memcached. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Separate method for handling reconnection logic
    private void reconnectToMemcached() {
        try {
            if (memcachedClient != null) {
                memcachedClient.shutdown(); // Close the existing client if necessary
            }

            // Reconnect to Memcached
            memcachedClient = new MemcachedClient(new InetSocketAddress("localhost", 11211));
            System.out.println("Reconnected to Memcached server successfully.");
            
        } catch (Exception e) {
            System.out.println("Failed to reconnect to Memcached server. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void evict(Object key) {
        System.out.println("Evicting key: " + key + " from Memcached");
        memcachedClient.delete(key.toString());
    }

    @Override
    public void clear() {
        System.out.println("Clear operation not supported for Memcached");
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = memcachedClient.get(key.toString());
        if (value != null) {
            System.out.println("Cache hit for key: " + key);
            return (T) value;
        } else {
            System.out.println("Cache miss for key: " + key);
            try {
                // Load the value using valueLoader and store it in the cache
                T newValue = valueLoader.call();
                put(key, newValue);
                return newValue;
            } catch (Exception e) {
                throw new RuntimeException("Value retrieval failed for key: " + key, e);
            }
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = memcachedClient.get(key.toString());
        if (value == null) {
            System.out.println("Cache miss for key: " + key);
            return null;
        }
        // Attempt to cast the value to the specified type
        try {
            return type.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Cached value is not of the expected type: " + type.getName(), e);
        }
    }
}
