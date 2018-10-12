package cn.asens.cache;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Asens
 */

@Service
public class DefaultTokenCache implements TokenCache{
    private Set<String> cache = new HashSet<>();

    @Override
    public synchronized boolean containsInvalidKey(String key) {
        return cache.contains(key);
    }

    @Override
    public synchronized void addInvalidKey(String key) {
        cache.add(key);
    }

    @Override
    public void removeInvalidKey(String key) {
        cache.remove(key);
    }
}
