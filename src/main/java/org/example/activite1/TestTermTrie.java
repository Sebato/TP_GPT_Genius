package org.example.activite1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestTermTrie {
    static TermTrie termTrie = new TermTrie();
    static String filepath = "/data/entries.txt";

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        //construction de la liste des termes à rechercher
        //on va récupérer aléatoirement des termes du fichier au fur et à mesure
        //et les stocker dans cette liste :

        List<String> nTerms = new ArrayList<>();

        //On veut 1000 termes et le fichier en contient : 3,275,157
        //la probabilite de prendre un terme du fichier est de : 1000/3275157

        double proba = 1000.0/3275157.0;


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

                //on ajoute le terme à la liste avec une probabilité de 1000/3275157
                if (Math.random() < proba && nTerms.size() < 1000) {
                    nTerms.add(term);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Temps de création de la structure : " + (endTime - startTime) + " ms\n");

//      mesurer les temps de recherche
//        termTrie.searchTime("Maria Callas", 1, true);
//
//        termTrie.searchTime("petit", 2,true);
//
//       termTrie.searchTime("petit chat", 2,true);
//
//        termTrie.searchTime("chat de", 3,false);
        long nbtermes = nTerms.size();
        System.out.println("nombre de termes à rechercher : "+nbtermes);

        // Méthode qui mesure le temps de recherche des termes
        long[] termtime = searchTermsAndMeasureTime(termTrie, nTerms);
        System.out.println("\n\n");
        // Méthode qui mesure le temps de recherche des mots préfixés
        long[] prefixtime = searchPrefixesAndMeasureTime(termTrie, nTerms);


        System.out.println("Temps global de recherche des "+nbtermes+" termes : " + termtime[0] + "ns");
        System.out.println("Temps moyen de recherche d'un terme : " + termtime[1] + " ns");

        System.out.println("Temps global de recherche des "+nbtermes+" prefixes : " + prefixtime[0] + " ns");
        System.out.println("Temps moyen de recherche des prefixes d'un terme : " + prefixtime[1] + " ns");
    }

    private static long[] searchTermsAndMeasureTime(TermTrie termTrie, List<String> terms) {
        System.out.println("Recherche de termes :");

        long startTime = System.nanoTime();

        for (String term : terms) {
            long termStartTime = System.nanoTime();
            int id = termTrie.findTermId(term);
            long termEndTime = System.nanoTime();

/*            System.out.println("Terme : " + term + ", ID : " + id +
                    ", Temps individuel : " + (termEndTime - termStartTime) + " ns");*/
        }

        long endTime = System.nanoTime();

        long globalTime = endTime - startTime;
        long averageTime = globalTime / terms.size();


        return new long[] {globalTime, averageTime};
    }

    private static long[] searchPrefixesAndMeasureTime(TermTrie termTrie, List<String> terms) {
        System.out.println("\nRecherche de mots préfixés :");

        long startTime = System.nanoTime();

        for (String term : terms) {
            long termStartTime = System.nanoTime();
            List<Map.Entry<String, Integer>> prefixResults = termTrie.findTermsByPrefix(term);
            long termEndTime = System.nanoTime();

            if (prefixResults != null) {
                /*System.out.println("Préfixe : " + term +
                        ", Résultats : " + prefixResults +
                        ", Temps individuel : " + (termEndTime - termStartTime) + " ns");*/
            } else {
//                System.out.println("Préfixe : " + term + ", Aucun résultat");
            }
        }

        long endTime = System.nanoTime();

        long globalTime = endTime - startTime;
        long averageTime = globalTime / terms.size();

        return new long[] {globalTime, averageTime};
    }
}
