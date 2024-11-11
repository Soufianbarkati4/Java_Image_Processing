package mam3.ipa.projet;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.media.jai.*;


public abstract class TraitementImage {

    protected Image img; //Image à utiliser
    protected Scanner s; //Scanner pour pouvoir lire les entrées claviers
    protected static int IMG_WIDTH; //constante représentant la largeur de l'image
    protected static int IMG_HEIGHT; //constante représentant la hauteur de l'image

    /**
     *
     * @param fn nom du fichier à utiliser
     * @return l'image à ouvrir
     * @throws ImageFailException si l'image n'existe pas
     */
    public static Image openFile(String fn) throws ImageFailException {
        RenderedOp ropimage; // a Rendered Operation object will contain the metadata and data
        ropimage = JAI.create("fileload", fn); // open the file
        BufferedImage bi = ropimage.getAsBufferedImage(); // BufferedImage will contain the data
        IMG_WIDTH = ropimage.getWidth(); // img width
        IMG_HEIGHT = ropimage.getHeight(); // img height
        ColorModel cm = ropimage.getColorModel();  //gray-scale or colored image ?

        //si l'image est en couleur
        if (cm.getColorSpace().getType() == ColorSpace.TYPE_RGB) {
            int[] px2;
            px2 = bi.getRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, null, 0, IMG_WIDTH);
            return new Image(px2, fn);
        }

        //si l'image est en nuance de gris
        else if (bi.getType() == BufferedImage.TYPE_BYTE_GRAY && cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY) {
            Raster r = ropimage.getData(); //contient les données brutes de l'image
            DataBufferByte db = (DataBufferByte) (r.getDataBuffer());
            byte[] px;
            px = db.getData();
            return new Image(px, fn);
        }
        //si l'image n'existe pas
        throw new ImageFailException(fn) ;
    }



    /**
     * Vérifie si le chemin entré par l'utilisateur est valide, c'est à dire s'il existe ou s'il ne s'agit pas d'un dossier.
     * Quitte le système sinon.
     * Static pour pouvoir l'utiliser dans toutes les classes
     *
     * @param p : chemin du fichier
     */
    public static void errorPath (Path p) {

        if (Files.isDirectory(p)) {
            System.out.println("Erreur : il ne s'agit pas d'un fichier. Relancez le programme en choisissant un fichier. ");
            System.exit(0);
        }
        if (!Files.exists(p)) {
            System.out.println("path : " + p );
            System.out.println("Erreur : le fichier n'existe pas. Relancez le programme en choisissant un fichier existant.");
            System.exit(0);
        }
    }

    /**
     * Crée un histogramme à deux colonnes (deux entrées)
     * @param tab : tableau d'entiers à deux dimensions
     */
    void makeHistogramme(int[][] tab) {

        String name= this.img.fn.split("\\.")[0]; //stocke uniquement le nom du fichier sans extension
        Path p = Paths.get(name+"-h.txt"); //Chemin du fichier

        // Création du fichier histogramme. Dans le cas où il existe déjà on l'efface
        try (BufferedWriter hist = Files.newBufferedWriter(p, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < tab.length; j++) {
                    hist.write(tab[j][i] + ";");
                }
                hist.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Méthode dont le but est de créer un nouveau fichier png à partir de l'image traité. Le nom de la nouvelle image
     * sera suivi du traitement effectué.
     * @param traitement : nom du traitement effectué à l'image.
     */
    protected abstract void enregistrementImage(String traitement);

    /**
     * Méthode abstraite qui va lancer le traitement nécessaire pour l'image.
     * @param img fichier à traiter
     * @param traitement traitement demandé
     */
    public abstract void lancerTraitement(Image img, String traitement);
    public abstract void menu();
    public abstract void lancerTraitementCG(String[] s, Image img);

    /**
     * Méthode principale de la classe dont le but est de permettre l'application des traitements sur les images
     * @param args arguments entrés par l'utilisateur
     */
    public static void main(String[] args) throws ImageFailException {

        int nbArgs = args.length;
        Scanner sc = new Scanner(System.in);
        InterfaceUtilisateur interfaceUtilisateur = new InterfaceUtilisateur();

        if (nbArgs == 0) { //Aucun argument n'a été rentré
            System.out.println("Veuillez entrer le nom du fichier que vous souhaitez traité : ");
            String name = sc.nextLine();
            //On vérifie que le fichier est accessible
            String pwd = System.getProperty("user.dir");
            Path p = Paths.get(pwd+"/"+name);
            TraitementImage.errorPath(p);

            TraitementImage t;
            try {
                Image img = TraitementImage.openFile(name);
                if (img.getType()) { //si boolean retourne true = image colorée
                    System.out.println("Votre image est colorée");
                    t = new TraitementImageColored(img);
                } else {
                    System.out.println("Votre image est en nuance de gris");
                    t = new TraitementImageGrey(img);
                }
                //On affiche le menu des différents choix selon la couleur de l'image
                t.menu();
                String[] choix = interfaceUtilisateur.choixTraitement();
                try {
                    t.lancerTraitementCG(choix, img);
                } catch (InputMismatchException e) {
                    System.err.println("Erreur vous n'avez pas sélectionné un entier possible ");

                }
            } catch (ImageFailException e) {
                System.err.println(e);

            }
        }
        else if (nbArgs == 1) { //seulement le nom a été rentré
            System.out.println("");
            System.out.println("Vous avez déjà saisis le nom de l'image en paramètre.");
            String name = args[0]; //on récupère le nom
            //On vérifie que le fichier est accessible
            String pwd = System.getProperty("user.dir");
            Path p = Paths.get(pwd+"/"+name);
            TraitementImage.errorPath(p);

            TraitementImage t;
            try {
                Image img = TraitementImage.openFile(name);
                if (img.getType()) {
                    t = new TraitementImageColored(img);
                } else {
                    t = new TraitementImageGrey(img);
                }
                t.menu();
                String[] choix = interfaceUtilisateur.choixTraitement();
                try {
                    t.lancerTraitementCG(choix, img);
                } catch (InputMismatchException e) {
                    System.err.println("Erreur vous n'avez pas sélectionné un entier possible ");
                }
            } catch (ImageFailException e) {
                System.err.println(e);
            }
        } else { //Dans le cas où plus d'un argument a été saisi
            String name = args[0]; //on récupère le nom
            //On vérifie que le fichier est accessible
            String pwd = System.getProperty("user.dir");
            Path p = Paths.get(pwd+"/"+name);
            TraitementImage.errorPath(p);

            TraitementImage t;
            try {
                Image img = TraitementImage.openFile(name);
                if (img.getType()) {
                    System.out.println("Votre image est colorée. Le traitement est en cours d'opération");
                    t = new TraitementImageColored(img);
                } else {
                    System.out.println("Votre image est en nuance de gris. Le traitement est en cours d'opération.");
                    t = new TraitementImageGrey(img);
                }
                //Pas besoin d'afficher le menu puisque les traitements ont déjà été choisis
                String[] choix = new String[nbArgs - 1]; //on recupère les traitements sans le nom
                for (int i = 0; i < nbArgs - 1; i++) {
                    choix[i] = args[i + 1];
                }
                t.lancerTraitementCG(choix, img);
            } catch (ImageFailException e) {
                System.err.println(e);
            }
        }
    }
}
