package src;

import src.cache.Cache;

public class Main {
    public static void main(String[] args) {
        Cache<String, Integer> cache = new Cache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.get("a");
        cache.put("d", 4);
        System.out.println(cache + " eldest=" + cache.eldestKey() + " newest=" + cache.newestKey()
                + " full=" + cache.isFull() + " capacity=" + cache.capacity());
    }
}