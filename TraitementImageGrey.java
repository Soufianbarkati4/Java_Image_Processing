package mam3.ipa.projet;

import java.awt.image.*;
import javax.media.jai.*;
import java.awt.Point;



/**
 * Classe qui traite et analyse les images en niveau de gris.
 * La classe étend de la classe TraitementImage
 */
public class TraitementImageGrey extends TraitementImage {

    /**
     * Constructeur
     */
    public TraitementImageGrey(Image img){
        this.img=img;
    }

    /**
     * Méthode dont le but est d'éclaircir chaque pixel de l'image traitée
     * @return Nouvelle image éclaircie
     */
    Image eclairage(){
        int px = this.img.pixelsGrey.length; //On récupère le nombre de pixels de l'image
        byte[] newPx= new byte[px]; //On crée les pixels pour la nouvelle image
        //On parcourt chaque pixel de l'image
        for (int i=0;i<px;i++){
            //On leur applique à chacun l'éclairage et on normalise également
            //OxFF valeur en base hexadécimale qui renvoie les 8 derniers bits
            double pxx = Math.sqrt(0xFF & this.img.pixelsGrey[i])*Math.sqrt(255); // On modifie le pixel pour appliquer l'eclairage : sqrt(p)*sqrt(255)
            newPx[i]=(byte)pxx;
        }
        //On retourne la nouvelle image avec les pixels éclaircis
        return new Image(newPx,this.img.fn);
    }

    /**
     * Méthode dont le but est d'assombrir chaque pixel de l'image
     * @return Nouvelle image assombrie
     */
    Image assombrissement(){
        int px = this.img.pixelsGrey.length;
        byte[] newPx = new byte[px];	//On crée les pixels pour la nouvelle image

        //On parcourt chaque pixel de l'image
        for(int i = 0; i < px ; i++){
            // On applique à chacun l'assombrissement puis on normalise
            //OxFF valeur en base hexadécimale qui renvoie les 8 derniers bits
            double pxx = (((double)(0xFF & this.img.pixelsGrey[i])) *
                    ((double) (0xFF & this.img.pixelsGrey[i])))/255.0;
            newPx[i] = (byte)pxx;
        }
        //On retourne la nouvelle image avec les pixels assombris
        return new Image(newPx,this.img.fn);
    }

    /**
     * Méthode dont le but est de faire l'analyse de l'image traité. Pour cela un histogramme sera créé et ce en
     * créant d'abord un tableau à deux entrées : en abscisse les degrés (entre 0 et 255) et en ordonnée
     * la fréquence d'apparition de ces degrès de couleurs.Plus tard, on fait appel à la méthode makeHistogramme()
     * pour créer notre histogramme.
     */
    void analyse(){
    //L'histogramme est en deux dimensions on crée alors deux colonnes représentant
        //abscisse et ordonnée
        int[] abs=new int[256];
        int[] ord= new int[256];

        for (int i=0;i<256;i++){
            //On crée la colonne des abscisses représentent les 256 premières valeurs
            abs[i]=i;
            //On initialise un compteur à 0 pour pouvoir recenser l'apparition des degrès de couleur
            int cpt=0;
            for (int j=0;j<this.img.pixelsGrey.length;j++){
                //Si degré de couleur appartient au tableau de pixels
                if(this.img.pixelsGrey[j]== (byte)(0xFF & i)){
                    cpt++; //on incrèmente le compteur
                }
            }
            //On crée la deuxième colonne de l'ordonnée
            ord[i]=cpt;
        }
        //On crée l'histogramme en utilisant la fonction que l'on retrouve dans la classe mère
        int[][] h={abs,ord};
        this.makeHistogramme(h);
}

    /**
     * Méthode dont le but est de créer un nouveau fichier png à partir de l'image traité. Le nom de la nouvelle image
     * sera suivi du traitement effectué.
     * @param traitement : nom du traitement effectué à l'image.
     */
    @Override
    public void enregistrementImage(String traitement){
        //On récupère le nom du fichier
        String[] name = this.img.fn.split("\\.");

        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,IMG_WIDTH,IMG_HEIGHT,1);
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,BufferedImage.TYPE_BYTE_GRAY);
        image.setData(Raster.createRaster(sm, new DataBufferByte(this.img.pixelsGrey, this.img.pixelsGrey.length), new Point()));
        JAI.create("filestore",image,name[0]+"-"+traitement+".png","PNG");
    }

    /**
     * Méthode dont le but est de créer une instance de la classe TraitementImageGrey et d'enregistrer
     * l'image à traiter.
     * @param img fichier à traiter
     * @param traitement traitement demandé
     */
    @Override
    public void lancerTraitement(Image img, String traitement){
        TraitementImageGrey t= new TraitementImageGrey(img);
        t.enregistrementImage(traitement);
        System.out.println("Le traitement suivant : "+ traitement+ " a été effectué avec succès!");
    }
    @Override
    public void menu(){
        System.out.println("Veuillez sélectionner l'opération que vous voulez effectuer : ");
        System.out.println("2-Assombrissement");;
        System.out.println("3-Eclairage");
        System.out.println("4-Analyse");
        System.out.println("5-Quitter le programme");
        System.out.println("6- Valider les choix déjà selectionnés");


    }
    @Override
    public void lancerTraitementCG(String[] s, Image img) {

        TraitementImageGrey t = new TraitementImageGrey(img);
        for (String traitement : s) {
            if (!traitement.equals("assombrissement") && !traitement.equals("analyse") && !traitement.equals("eclairage")) {
                System.out.println("Erreur vous n'avez pas sélectionné un traitement valide pour cette image");

            } else {
                switch (traitement) {
                    case "assombrissement": {
                        Image newImg = t.assombrissement();
                        t.lancerTraitement(newImg, traitement);
                        break;
                    }
                    case "analyse": {
                        //La méthode analyse ne renvoie pas d'image c'est pour cela que l'on a pas
                        //besoin d'instancier un objet de la classe Image
                        t.analyse();
                        break;
                    }
                    case "eclairage": {
                        Image newImg = t.eclairage();
                        t.lancerTraitement(newImg, traitement);
                        break;
                    }
                }
            }

        }
    }


}

