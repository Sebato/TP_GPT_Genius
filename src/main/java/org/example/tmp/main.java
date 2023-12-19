package org.example.tmp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


 class TestTermTrie {
    public static void main(String[] args) {
        // Mesurer le temps de création des structures
        long startTime = System.currentTimeMillis();

        TermTrie termTrie = new TermTrie();

        // Charger le fichier et construire la trie
        try (BufferedReader br = new BufferedReader(new FileReader("data/entries.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) {
                    continue;
                }
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String term = parts[1].replaceAll("\"", "");
                termTrie.addTerm(term, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mesurer le temps après la création des structures
        long endTime = System.currentTimeMillis();
        System.out.println("Temps de création des structures : " + (endTime - startTime) + " ms");

        // Tester la fonction 1
        System.out.println(termTrie.findTermId("Maria Callas")); // devrait afficher 16
        System.out.println(termTrie.findTermId("MotInexistant")); // devrait afficher -1

        // Tester la fonction 2
        List<Map.Entry<String, Integer>> prefixResults = termTrie.findTermsByPrefix("Dead");
        for (Map.Entry<String, Integer> entry : prefixResults) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}

