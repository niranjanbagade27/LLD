package com.example.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class Node {
    private String key;
    private int data;
    private int expiration;
    private int priority;
}
