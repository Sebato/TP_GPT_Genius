package org.example.activite3;

class Noeud {
    int valeur;
    Noeud gauche;
    Noeud droite;
    int hauteur;

    // Constructeur
    public Noeud(int valeur) {
        this.valeur = valeur;
        this.gauche = null;
        this.droite = null;
        this.hauteur = 1;
    }
}

public class AVLTree {
    // Fonction pour calculer la hauteur d'un noeud
    private int hauteur(Noeud N) {
        return (N == null) ? 0 : N.hauteur;
    }

    // Fonction pour calculer le maximum de deux entiers
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Fonction pour effectuer une rotation à droite simple
    private Noeud faireSimpleRotationADroite(Noeud y) {
        Noeud x = y.gauche;
        Noeud T2 = x.droite;

        // Effectuer la rotation
        x.droite = y;
        y.gauche = T2;

        // Mettre à jour les hauteurs
        y.hauteur = max(hauteur(y.gauche), hauteur(y.droite)) + 1;
        x.hauteur = max(hauteur(x.gauche), hauteur(x.droite)) + 1;

        return x;
    }

    // Fonction pour effectuer une rotation à gauche simple
    private Noeud faireSimpleRotationAGauche(Noeud x) {
        Noeud y = x.droite;
        Noeud T2 = y.gauche;

        // Effectuer la rotation
        y.gauche = x;
        x.droite = T2;

        // Mettre à jour les hauteurs
        x.hauteur = max(hauteur(x.gauche), hauteur(x.droite)) + 1;
        y.hauteur = max(hauteur(y.gauche), hauteur(y.droite)) + 1;

        return y;
    }

    // Fonction pour effectuer une rotation à droite double
    private Noeud faireDoubleRotationADroite(Noeud z) {
        z.gauche = faireSimpleRotationAGauche(z.gauche);
        return faireSimpleRotationADroite(z);
    }

    // Fonction pour effectuer une rotation à gauche double
    private Noeud faireDoubleRotationAGauche(Noeud z) {
        z.droite = faireSimpleRotationADroite(z.droite);
        return faireSimpleRotationAGauche(z);
    }

    // Fonction pour équilibrer un arbre AVL après une insertion
    private Noeud equilibrerArbre(Noeud racine) {
        // Mettre à jour la hauteur du noeud actuel
        racine.hauteur = 1 + max(hauteur(racine.gauche), hauteur(racine.droite));

        int equilibre = hauteur(racine.gauche) - hauteur(racine.droite);

        // Rotation à droite simple
        if (equilibre > 1 && hauteur(racine.gauche.gauche) >= hauteur(racine.gauche.droite))
            return faireSimpleRotationADroite(racine);

        // Rotation à gauche simple
        if (equilibre < -1 && hauteur(racine.droite.droite) >= hauteur(racine.droite.gauche))
            return faireSimpleRotationAGauche(racine);

        // Rotation à droite double
        if (equilibre > 1 && hauteur(racine.gauche.gauche) < hauteur(racine.gauche.droite)) {
            racine.gauche = faireSimpleRotationAGauche(racine.gauche);
            return faireSimpleRotationADroite(racine);
        }

        // Rotation à gauche double
        if (equilibre < -1 && hauteur(racine.droite.droite) < hauteur(racine.droite.gauche)) {
            racine.droite = faireSimpleRotationADroite(racine.droite);
            return faireSimpleRotationAGauche(racine);
        }

        return racine;
    }

    // Fonction pour insérer une valeur dans l'arbre AVL
    private Noeud inserer(Noeud racine, int valeur) {
        // Insérer normalement comme dans un arbre binaire de recherche
        if (racine == null)
            return new Noeud(valeur);

        if (valeur < racine.valeur)
            racine.gauche = inserer(racine.gauche, valeur);
        else if (valeur > racine.valeur)
            racine.droite = inserer(racine.droite, valeur);
        else // Les valeurs égales ne sont pas autorisées
            return racine;

        // Équilibrer l'arbre après l'insertion
        return equilibrerArbre(racine);
    }

    // Fonction pour trouver le successeur d'un noeud
    private Noeud trouverSuccesseur(Noeud racine) {
        Noeud courant = racine;
        while (courant.gauche != null)
            courant = courant.gauche;
        return courant;
    }

    // Fonction pour supprimer un noeud de l'arbre AVL
    private Noeud supprimer(Noeud racine, int valeur) {
        // Étape de suppression normale comme dans un arbre binaire de recherche
        if (racine == null)
            return racine;

        if (valeur < racine.valeur)
            racine.gauche = supprimer(racine.gauche, valeur);
        else if (valeur > racine.valeur)
            racine.droite = supprimer(racine.droite, valeur);
        else {
            // Noeud avec un seul enfant ou sans enfant
            if (racine.gauche == null || racine.droite == null) {
                Noeud temp = (racine.gauche != null) ? racine.gauche : racine.droite;

                // Aucun enfant
                if (temp == null) {
                    temp = racine;
                    racine = null;
                } else // Un enfant
                    racine = temp; // Copier le contenu du noeud non vide

                temp = null;
            } else {
                // Noeud avec deux enfants
                Noeud temp = trouverSuccesseur(racine.droite);
                racine.valeur = temp.valeur;
                racine.droite = supprimer(racine.droite, temp.valeur);
            }
        }

        // Si l'arbre avait un seul noeud, le retourner
        if (racine == null)
            return racine;

        // Équilibrer l'arbre après la suppression
        return equilibrerArbre(racine);
    }

    // Fonction pour afficher l'arbre AVL de manière récursive
    private void afficherArbre(Noeud racine, int espace) {
        if (racine == null)
            return;

        espace += 10;

        // Afficher le sous-arbre droit
        afficherArbre(racine.droite, espace);

        System.out.println();
        for (int i = 10; i < espace; i++)
            System.out.print(" ");
        System.out.println(racine.valeur);

        // Afficher le sous-arbre gauche
        afficherArbre(racine.gauche, espace);
    }

    // Fonction pour libérer la mémoire utilisée par l'arbre AVL
    private void libererMemoire(Noeud racine) {
        if (racine == null)
            return;

        libererMemoire(racine.gauche);
        libererMemoire(racine.droite);

        racine = null;
    }

    // Fonction principale
    public static void main(String[] args) {
        AVLTree arbreAVL = new AVLTree();
        Noeud racine = null;

        racine = arbreAVL.inserer(racine, 10);
        racine = arbreAVL.inserer(racine, 20);
        racine = arbreAVL.inserer(racine, 30);

        System.out.println("Arbre AVL après insertion :\n");
        arbreAVL.afficherArbre(racine, 0);

        racine = arbreAVL.supprimer(racine, 20);

        System.out.println("\nArbre AVL après suppression :\n");
        arbreAVL.afficherArbre(racine, 0);

        racine = arbreAVL.inserer(racine, 25);
        racine = arbreAVL.inserer(racine, 3);

        arbreAVL.afficherArbre(racine, 0);
        arbreAVL.libererMemoire(racine);
    }
}

