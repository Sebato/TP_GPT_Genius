package org.example.activite1;

import java.util.*;

public class TermTrie {
    private TrieNode root;

    public TermTrie() {
        this.root = new TrieNode();
    }

    public void addTerm(String term, int id) {
        TrieNode node = root;
        for (int i = 0; i < term.length(); i += 4) {
            String pair = term.substring(i, Math.min(i + 4, term.length()));
            node.children.putIfAbsent(pair, new TrieNode());
            node = node.children.get(pair);
        }
        node.id = id;
    }

    public int findTermId(String term) {
        TrieNode node = root;
        for (int i = 0; i < term.length(); i += 4) {
            String pair = term.substring(i, Math.min(i + 4, term.length()));
            if (!node.children.containsKey(pair)) {
                return -1;
            }
            node = node.children.get(pair);
        }
        return node.id;
    }

    public List<Map.Entry<String, Integer>> findTermsByPrefix(String prefix) {
        TrieNode node = root;
        List<Map.Entry<String, Integer>> result = new LinkedList<>();

        for (int i = 0; i < prefix.length(); i += 4) {
            String pair = prefix.substring(i, Math.min(i + 4, prefix.length()));
            if (!node.children.containsKey(pair)) {
                return null;
            }
            node = node.children.get(pair);
        }

        collectTerms(node, prefix, result);
        return result;
    }

    private void collectTerms(TrieNode node, String currentTerm, List<Map.Entry<String, Integer>> result) {
        if (node.id != -1) {
            result.add(Map.entry(currentTerm, node.id));
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            collectTerms(entry.getValue(), currentTerm + entry.getKey(), result);
        }
    }
}