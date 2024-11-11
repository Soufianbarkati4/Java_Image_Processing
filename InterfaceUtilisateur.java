package mam3.ipa.projet;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe qui interargit avec l'utilisateur.
 * @author
 */

public class InterfaceUtilisateur {
    private String fn;
    private String traitement;
    private Scanner s = new Scanner(System.in);


    public InterfaceUtilisateur() {
        acceuil();
    }

    /**
     * Présentation du programme
     */
    void acceuil() {
        System.out.println("Bienvenue dans ce programme qui vous permettra de réaliser des opérations sur vos images.");
        System.out.println("Vous aurez le choix parmis les opérations suivantes :");
        System.out.println("Pour une image grisée : ");
        System.out.println("    -Assombrissement, analyse ou éclairage de l'image.");
        System.out.println("Pour une image en couleurs : ");
        System.out.println("    -Transformation en niveau de gris.");
    }

    /**
     * Affiche les différentes options disponibles de traitement d'image pour une image en couleurs
     * <p>
     * void menuImageColored(){
     * System.out.println("Veuillez sélectionner l'opération que vous voulez effectuer : ");
     * System.out.println("1- Transformation en niveau de gris");
     * System.out.println("5- Quitter le programme");
     * System.out.println("6- Valider les choix déjà selectionnés");
     * }
     * <p>
     * <p>
     * Affiche les différentes options disponibles de traitement et d'analyse d'image pour une image en nuance de gris
     * <p>
     * void menuImageGrey(){
     * System.out.println("Veuillez sélectionner l'opération que vous voulez effectuer : ");
     * System.out.println("2-Assombrissement");;
     * System.out.println("3-Eclairage");
     * System.out.println("4-Analyse");
     * System.out.println("5-Quitter le programme");
     * System.out.println("6- Valider les choix déjà selectionnés");
     * <p>
     * }
     **/
    public String[] choixTraitement() {
        int choix = s.nextInt(); //Entrée d'un entier
        System.out.println("Vous avez choisis le numéro "+ choix + ". Appuyez sur 6 pour valider.");
        //ArrayList pour pouvoir ajouter un nombre d'élèments indeterminé
        ArrayList<String> choixTraitement = new ArrayList<>();
        while (choix != 6) { //boucle tant que l'utilisateur n'a pas validé les choix
            switch (choix) {
                case 1: {
                    choixTraitement.add("transformation");
                    break;
                }
                case 2: {
                    choixTraitement.add("assombrissement");
                    break;
                }
                case 3: {
                    choixTraitement.add("eclairage");
                    break;
                }
                case 4: {
                    choixTraitement.add("analyse");
                    break;
                }
                case 5: {
                    System.out.println("Vous avez choisis de quitter le programme");
                    System.exit(0);
                }
            }
            choix = s.nextInt(); //Car l'utilisateur peut rajouter des options
        }
        //Une fois toutes les infos recueillies on transforme l'arrayList en tableau après avoir récupéré la taille de cette dernière
        int size = choixTraitement.size();
        return choixTraitement.toArray(new String[size]);

    }
}

    /**
     * Méthode dont le but est de lancer les traitements demandés dans le cas d'une image colorée
     * @param s traitements choisis par l'utilisateur
     * @param img image traitée

    void lancerTraitementColored(String[] s, Image img){

        TraitementImageColored t= new TraitementImageColored(img);
        for(String traitement : s) {
            if (traitement.equals("transformation")) {
                Image newImg = t.griser();
                t.lancerTraitement(newImg, traitement);
            } else{
                System.out.println("Erreur vous n'avez pas sélectionné un traitement valide pour cette image");
                lancerTraitementColored(s,img);
            }
        }

    }

    /**
     * Méthode dont le but est de lancer les traitement demandés dans le cas d'image grises.
     * @param s Traitements choisis par l'utilisateur
     * @param img image traitée

    void lancerTraitementGrey(String[] s, Image img) {

        TraitementImageGrey t = new TraitementImageGrey(img);
        for (String traitement : s) {
            if (!traitement.equals("assombrissement") && !traitement.equals("analyse") && !traitement.equals("eclairage")) {
                System.out.println("Erreur vous n'avez pas sélectionné un traitement valide pour cette image");
                lancerTraitementGrey(s, img);
            } else {
                switch (traitement) {
                    case "assombrissement": {
                        Image newImg = t.assombrissement();
                        t.lancerTraitement(newImg, traitement);
                    }
                    case "analyse": {
                        //La méthode analyse ne renvoie pas d'image c'est pour cela que l'on a pas
                        //besoin d'instancier un objet de la classe Image
                        t.analyse();
                        System.out.println("L'analyse a bien été appliquée à votre image");
                    }
                    case "eclairage": {
                        Image newImg = t.eclairage();
                        t.lancerTraitement(newImg, traitement);
                    }
                }
            }

        }
    }
    **/
    /**
     * Méthode qui lance les traitements en fonction de la coloration de l'image.
     * @throws ImageFailException

    void lancement() throws ImageFailException{
        System.out.println("Veuillez entrer le nom de l'image que vous souhaitez modifier");
        String rep = s.nextLine();
        //Vérification que l'image est bien accessible
        Path p = Paths.get(rep);
        TraitementImage.errorPath(p);

        Image img = TraitementImage.openFile(rep);
        if (img.isColored){
            TraitementImageColored t =new TraitementImageColored(img);
            t.menu();
            String[] choix = choixTraitement();
            lancerTraitementColored(choix,img);
        }
        else{
            TraitementImageGrey t= new TraitementImageGrey(img);
            t.menu();
            String[] choix= choixTraitement();
            lancerTraitementGrey(choix,img);
        }
    }
}
**/
