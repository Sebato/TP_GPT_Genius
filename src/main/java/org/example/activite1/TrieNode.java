package org.example.activite1;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    Map<String, TrieNode> children;
    int id;

    public TrieNode() {
        this.children = new HashMap<>();
        this.id = -1;
    }
}




