package org.example.activite1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestTermTrie {
    public static void main(String[] args) {
        TermTrie termTrie = new TermTrie();
        String filepath = "/data/entries.txt";

        long startTime = System.currentTimeMillis();

        // Charger le fichier et construire la trie
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+filepath))) {
            String line;
            System.out.println("Chargement du fichier...");
            while ((line = br.readLine()) != null) {
                //ignore empty lines or lines starting with special characters
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) {
                    continue;
                }
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String term = parts[1];
                termTrie.addTerm(term, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Temps de création de la structure : " + (endTime - startTime) + " ms");

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
