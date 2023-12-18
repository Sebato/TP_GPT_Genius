package org.example.activite1;

import java.util.*;
import java.util.function.Function;

public class TermTrie {
    private TrieNode root;

    public TermTrie() {
        this.root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
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

    //methode d'affichage du temps de recherche
    public void searchTime(String term, int methode) {

        int tmpid = -1;
        long startTime = 0;
        long endTime = 0;
        String str = "";
        switch (methode){
            case 1:
                //recherche normale
                str = "Recherche simple du terme "+term;
                startTime = System.currentTimeMillis();
                tmpid = findTermId(term);
                endTime = System.currentTimeMillis();

                str += "\n\tID : "+tmpid+"\n\tTemps de recherche : " + (endTime - startTime) + " ms\n";
            break;

            case 2:
                //recherche de mots composés
                str = "Recherche du mot composé : "+term;
                startTime = System.currentTimeMillis();
                tmpid = findCompositeTermId(term);
                endTime = System.currentTimeMillis();

                if (tmpid == -2) {
                    str += "\n\tLe terme '"+term+"' n'est pas composé\n";
                    break;
                }else
                    str += "\n\tID : "+tmpid+"\n\tTemps de recherche : " + (endTime - startTime) + " ms\n";
            break;

            case 3:
                //recherche : savoir si un terme entré par l'utilisateur est un préfixe d'un ou plusieurs mots composés
                str = "Recherche du préfixe : "+term;
                StringBuilder res = new StringBuilder("Mots composés préfixés :");
                startTime = System.currentTimeMillis();
                List<Map.Entry<String, Integer>> compositeTerms = findCompositeTermsByPrefix(term);
                endTime = System.currentTimeMillis();

                if (compositeTerms != null) {
                    for (Map.Entry<String, Integer> entry : compositeTerms) {
                        res.append("\n\t").append(entry.getKey()).append(" => ").append(entry.getValue());
                    }
                } else {
                    System.out.println("Aucun terme composé avec ce préfixe.");
                }

                str += "\n\tTemps de recherche : " + (endTime - startTime) + " ms\n";
                str += res;

            break;

            default:
                System.out.println("Methode inconnue");
            break;
        }

        System.out.println(str);
    }

    //1) savoir si un terme est un mot composé appartenant à F, et obtenir son id. (ça retourne un id ou -1)
    public int findCompositeTermId(String term) {
        TrieNode node = root;
        String[] words = term.split(" ");
        if (words.length == 1) {
            return -2;  // Un seul mot n'est pas un mot composé
        }
        for (String word : words) {
            if (!node.children.containsKey(word)) {
                return -1;  // Au moins un mot n'est pas présent dans le trie, donc le terme n'est pas composé
            }
            node = node.children.get(word);
        }

        return node.id;
    }


    //2) savoir si un terme entré par l'utilisateur est un préfixe d'un ou plusieurs mots composés (ça retourne une liste de paires (mot composé, id)) ou null si vide
    public List<Map.Entry<String, Integer>> findCompositeTermsByPrefix(String prefix) {
        TrieNode node = root;
        List<Map.Entry<String, Integer>> result = new LinkedList<>();
        String[] prefixWords = prefix.split(" ");

        for (String prefixWord : prefixWords) {
            if (!node.children.containsKey(prefixWord)) {
                return null;  // Au moins un mot du préfixe n'est pas présent dans le trie
            }
            node = node.children.get(prefixWord);
        }

        collectCompositeTerms(node, prefix, result);
        return result.isEmpty() ? null : result;
    }

    private void collectCompositeTerms(TrieNode node, String currentTerm, List<Map.Entry<String, Integer>> result) {
        if (node.id != -1) {
            result.add(Map.entry(currentTerm, node.id));
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            collectCompositeTerms(entry.getValue(), currentTerm + " " + entry.getKey(), result);
        }
    }

}