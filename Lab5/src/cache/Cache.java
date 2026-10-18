package src.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public Cache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    public boolean isFull() {
        return size() == capacity;
    }

    public K eldestKey() {
        return isEmpty() ? null : keySet().iterator().next();
    }

    public K newestKey() {
        K last = null;
        for (K key : keySet()) last = key;
        return last;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}