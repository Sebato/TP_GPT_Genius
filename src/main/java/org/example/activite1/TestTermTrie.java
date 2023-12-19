package org.example.activite1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestTermTrie {
    static TermTrie termTrie = new TermTrie();
    static String filepath = "/data/entries.txt";

    public static void main(String[] args) {

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

//      mesurer les temps de recherche
        termTrie.searchTime("Maria Callas", 1, true);

        termTrie.searchTime("petit", 2,true);

       termTrie.searchTime("petit chat", 2,true);

        termTrie.searchTime("chat de", 3,false);




        // Méthode qui récupère n termes pris aléatoirement du fichier
        List<String> randomTerms = getRandomTerms(termTrie, 5);
        System.out.println("Termes aléatoires : " + randomTerms);

        // Méthode qui mesure le temps de recherche des termes
        searchTermsAndMeasureTime(termTrie, randomTerms);

        // Méthode qui mesure le temps de recherche des mots préfixés
        searchPrefixesAndMeasureTime(termTrie, randomTerms);
    }

    private static List<String> getRandomTerms(TermTrie termTrie, int n) {
        List<String> allTerms = getAllTermsFromFile(System.getProperty("user.dir")+filepath);
        Random random = new Random();
        List<String> randomTerms = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int randomIndex = random.nextInt(allTerms.size());
            randomTerms.add(allTerms.get(randomIndex));
        }

        return randomTerms;
    }

    private static List<String> getAllTermsFromFile(String filename) {
        List<String> terms = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String term = parts[1];
                terms.add(term);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return terms;
    }

    private static void searchTermsAndMeasureTime(TermTrie termTrie, List<String> terms) {
        System.out.println("Recherche de termes :");

        long startTime = System.nanoTime();

        for (String term : terms) {
            long termStartTime = System.nanoTime();
            int id = termTrie.findTermId(term);
            long termEndTime = System.nanoTime();

            System.out.println("Terme : " + term + ", ID : " + id +
                    ", Temps individuel : " + (termEndTime - termStartTime) + " ns");
        }

        long endTime = System.nanoTime();
        System.out.println("Temps global de recherche des termes : " + (endTime - startTime) + " ns");
    }

    private static void searchPrefixesAndMeasureTime(TermTrie termTrie, List<String> terms) {
        System.out.println("\nRecherche de mots préfixés :");

        long startTime = System.nanoTime();

        for (String term : terms) {
            long termStartTime = System.nanoTime();
            List<Map.Entry<String, Integer>> prefixResults = termTrie.findTermsByPrefix(term);
            long termEndTime = System.nanoTime();

            if (prefixResults != null) {
                System.out.println("Préfixe : " + term +
                        ", Résultats : " + prefixResults +
                        ", Temps individuel : " + (termEndTime - termStartTime) + " ns");
            } else {
                System.out.println("Préfixe : " + term + ", Aucun résultat");
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Temps global de recherche des préfixes : " + (endTime - startTime) + " ns");
    }
}
