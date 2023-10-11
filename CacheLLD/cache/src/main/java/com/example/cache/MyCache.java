package com.example.cache;

public interface MyCache {

    int get(String key);

    void set(String key, int value, int expiration, int priority);

}
