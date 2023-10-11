package com.example.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

public class AllInOne {
    class Node{
        String key;
        int value;
        int priority;
        int expiration;
        Node(String k, int v, int p, int e){
            this.key = k;
            this.value = v;
            this.priority = p;
            this.expiration = e;
        }
    }

    HashMap<String, Node> dataMap = new HashMap<>();
    TreeMap<Integer, LinkedHashSet<String>> priorityLruMap = new TreeMap<>();
    TreeMap<Integer, HashSet<String>> expirationMap = new TreeMap<>();
    int capacity;
    int threshold;

    AllInOne(int cap, int thr){
        capacity = cap;
        threshold = thr;
    }

    public int get(String key){
        if(dataMap.containsKey(key)){
            Node node = dataMap.get(key);
            removeFromPriorityMap(node.priority, node.key);
            addToPriorityMap(node.priority, node.key);
            return node.value;
        }else{
            return -1;
        }
    }

    public void set(String k, int v, int p, int e){
        Node node = new Node(k, v, p, e);
        if(dataMap.containsKey(k)){
            removeFromAllMaps(k);
        }else{
            if(isFull()){
                evict(e);
            }
        }
        addToAllMaps(node);
    }

    public void evict(int currTime){
        if(!evictFromExpirationMap(currTime)) {
            evictFromPriorityLRUMap();
        }
    }

    private void removeFromAllMaps(String k) {
        Node removed = dataMap.get(k);
        removeFromDataMap(removed.key);
        removeFromPriorityMap(removed.priority, removed.key);
        removeFromExpirationMap(removed.expiration, removed.key);
    }

    private void addToAllMaps(Node node) {
        addInDataMap(node);
        addToPriorityMap(node.priority, node.key);
        addToExpirationMap(node.expiration, node.key);
    }

    private boolean isFull(){
        return dataMap.size() >= capacity;
    }

    private void addToExpirationMap(int time, String key){
        expirationMap.computeIfAbsent(time, e -> new HashSet<>()).add(key);
    }

    private void removeFromExpirationMap(int time, String key){
        expirationMap.get(time).remove(key);
        if(expirationMap.get(time).size() == 0){
            expirationMap.remove(time);
        }
    }

    private boolean evictFromExpirationMap(int currTime){
        String key = expirationMap.firstEntry().getValue().iterator().next();
        Node node = dataMap.get(key);
        if(node.expiration + threshold < currTime){
            expirationMap.firstEntry().getValue().remove(key);
            if(expirationMap.firstEntry().getValue().size() == 0){
                expirationMap.remove(node.expiration);
            }
            return true;
        }
        return false;
    }

    private void addToPriorityMap(int priority, String key){
        priorityLruMap.computeIfAbsent(priority, e -> new LinkedHashSet<>()).add(key);
    }

    private void evictFromPriorityLRUMap(){
        String key = priorityLruMap.firstEntry().getValue().iterator().next();
        priorityLruMap.firstEntry().getValue().remove(key);
        if(priorityLruMap.firstEntry().getValue().size() == 0){
            Map.Entry<Integer, LinkedHashSet<String>> entry = priorityLruMap.firstEntry();
            priorityLruMap.remove(entry.getKey());
        }
    }

    private void removeFromPriorityMap(int priority, String key){
        priorityLruMap.get(priority).remove(key);
        if(priorityLruMap.get(priority).size() == 0){
            priorityLruMap.remove(priority);
        }
    }

    private void addInDataMap(Node node){
        dataMap.put(node.key, node);
    }

    private void removeFromDataMap(String key){
        dataMap.remove(key);
    }



}
