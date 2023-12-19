package org.example.tmp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<String, TrieNode> children;
    int id;

    public TrieNode() {
        this.children = new HashMap<>();
        this.id = -1;
    }
}

public class TermTrie {
    private TrieNode root;

    public TermTrie() {
        this.root = new TrieNode();
    }

    public void addTerm(String term, int id) {
        TrieNode node = root;
        String[] words = term.split(" ");
        for (String word : words) {
            node.children.putIfAbsent(word, new TrieNode());
            node = node.children.get(word);
        }
        node.id = id;
    }

    public int findTermId(String term) {
        TrieNode node = root;
        String[] words = term.split(" ");
        for (String word : words) {
            if (!node.children.containsKey(word)) {
                return -1;
            }
            node = node.children.get(word);
        }
        return node.id;
    }

    public List<Map.Entry<String, Integer>> findTermsByPrefix(String prefix) {
        TrieNode node = root;
        List<Map.Entry<String, Integer>> result = new LinkedList<>();
        String[] prefixWords = prefix.split(" ");

        for (String prefixWord : prefixWords) {
            if (!node.children.containsKey(prefixWord)) {
                return null;
            }
            node = node.children.get(prefixWord);
        }

        collectTerms(node, prefix, result);
        return result;
    }

    private void collectTerms(TrieNode node, String currentTerm, List<Map.Entry<String, Integer>> result) {
        if (node.id != -1) {
            result.add(Map.entry(currentTerm, node.id));
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            collectTerms(entry.getValue(), currentTerm + " " + entry.getKey(), result);
        }
    }
}
