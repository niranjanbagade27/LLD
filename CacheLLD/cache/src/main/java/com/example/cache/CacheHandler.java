package com.example.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeMap;

public class CacheHandler implements MyCache {
    private HashMap<String, Node> dataMap;
    private TreeMap<Integer, LinkedHashSet<String>> priorityLruMap;
    private TreeMap<Integer, HashSet<String>> expirationMap;

    private int threshold;
    private int capacity;

    public CacheHandler(int capacity, int threshold) {
        this.capacity = capacity;
        this.threshold = threshold;
        this.dataMap = new HashMap<>();
        this.priorityLruMap = new TreeMap<>();
        this.expirationMap = new TreeMap<>();
    }

    @Override
    public int get(String key) {
        if (dataMap.containsKey(key)) {
            System.out.println(key + " is known.");
            Node node = dataMap.get(key);
            removeFromPriorityLRU(node);
            addToPriorityLRU(node);
            return node.getData();
        } else {
            System.out.println(key + " doesnot exist.");
            return -1;
        }
    }

    private void addToPriorityLRU(Node node) {
        priorityLruMap.computeIfAbsent(node.getPriority(), k -> new LinkedHashSet<>()).add(node.getKey());
    }

    private void removeFromPriorityLRU(Node node) {
        priorityLruMap.get(node.getPriority()).remove(node.getKey());
    }

    @Override
    public void set(String key, int value, int priority, int expiration) {
        if (dataMap.containsKey(key)) {
            System.out.println("Replacing the node");
            // replace the existing element
            removeFromAllMaps(key);
        } else {
            // evict by priority and then LRU
            if(isFull())
                evict(expiration);
        }
        Node node = new Node(key, value, expiration, priority);
        System.out.println("Adding "+node);
        addInAllMaps(node);
        System.out.println(dataMap);
    }

    private void evictExpired(int currentTime, String potentialExporedKey) {
        if (dataMap.get(potentialExporedKey).getExpiration() + threshold < currentTime) {
            removeFromAllMaps(potentialExporedKey);
        }
    }

    private boolean isFull() {
        return dataMap.size() >= capacity;
    }

    private void addInAllMaps(Node node) {
        addToPriorityLRU(node);
        addToExpirationMap(node);
        dataMap.put(node.getKey(), node);
    }

    private void addToExpirationMap(Node node) {
        expirationMap.computeIfAbsent(node.getExpiration(), k -> new HashSet<>()).add(node.getKey());
    }

    private void removeFromAllMaps(String key) {
        Node nodeToBeRemoved = dataMap.get(key);
        priorityLruMap.get(nodeToBeRemoved.getPriority()).remove(key);

        if (priorityLruMap.get(nodeToBeRemoved.getPriority()).size() == 0) {
            priorityLruMap.remove(nodeToBeRemoved.getPriority());
        }

        expirationMap.get(nodeToBeRemoved.getExpiration()).remove(key);

        if (expirationMap.get(nodeToBeRemoved.getExpiration()).size() == 0) {
            expirationMap.remove(nodeToBeRemoved.getExpiration());
        }

        dataMap.remove(key);
    }

    protected void evict(int currTime) {
        // try to evict expired
        String potentialCandidate = expirationMap.firstEntry().getValue().iterator().next();
        evictExpired(currTime, potentialCandidate);
        if (isFull()) {
            System.out.println("None expired, evicting LRU with least priority ");
            // try to remove LRU
            potentialCandidate = priorityLruMap.firstEntry().getValue().iterator().next();
            removeFromAllMaps(potentialCandidate);
        } else {
            System.out.println("Removed expired ");
        }
    }

    public static void main(String[] args) {
        CacheHandler cache = new CacheHandler(3, 500);
        cache.set("A", 5, 1, 1000);
        cache.set("B", 15, 5, 500);
        cache.set("C", 0, 5, 2000);
        cache.set("D", 1, 5, 2000);
        cache.set("E", 10, 5, 3000);
        System.out.println(cache.get("C"));
        cache.set("F", 15, 5, 1000);
        cache.set("G", 0, 5, 2000);
        cache.set("H", 1, 1, 2000);
        System.out.println(cache.get("D"));
        cache.set("I", 10, 2, 2000);
        cache.set("E", 10, 2, 2000);
        cache.set("M", 10, 1, 3000);
    }
}
