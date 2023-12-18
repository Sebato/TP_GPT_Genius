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
                //retirer les guillemets
                String term = parts[1].replaceAll("\"", "");
                termTrie.addTerm(term, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Temps de création de la structure : " + (endTime - startTime) + " ms\n");

        //mesurer les temps de recherche
        termTrie.searchTime("Maria Callas", 1);

        termTrie.searchTime("petit", 2);

        termTrie.searchTime("petit chat", 2);

        termTrie.searchTime("chat de", 3);



    }
}
