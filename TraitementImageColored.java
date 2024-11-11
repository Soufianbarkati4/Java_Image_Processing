package mam3.ipa.projet;

import java.awt.image.*;
import javax.media.jai.*;




public class TraitementImageColored extends TraitementImage {

    /**
     * Constructeur
     */
    public TraitementImageColored(Image img){
        this.img=img;
    }

    /**
     * Méthode dont le but est de renvoyer l'image reçu en une nouvelle image grisée.
     * La logique utilisée est la même que pour les méthodes d'éclaircissement et d'assombrissement
     * de la classe TraitementImageGrey.
     * @return nouvelle image grisée.
     */
    Image griser(){
        int px= this.img.pixelsColored.length;
        int[] newPx = new int[px]; //On crée les pixels pour la nouvelle image
        //On parcourt chaque pixel de l'image
        for (int i=0; i<px;i++){
            // On applique à chacun le grisement en utilisant la méthode de moyenne pondérée
            //OxFF valeur en base hexadécimale qui renvoie les 8 derniers bits
            newPx[i]= (byte)(0xFF & moyennePonderee(this.img.pixelsColored[i]));
        }
        //On retourne la nouvelle image avec les pixels assombris
        return new Image(newPx,this.img.fn);
    }


    /**
     * Méthode dont le but est de calculer la moyenne pondérée en prenant un entier correspondant
     * à un pixel et en le décomposant selon les trois canaux de couleurs RGB.
     * @param px entier correspondant à un pixel
     * @return la valeur du pixel grisé en byte
     */
    static byte moyennePonderee(int px){
        //Pour le pixel considéré on le décompose selon les trois canaux de couleurs
        int r= (px>>16) & 0xFF;    //canal rouge
        int g= (px>>8) & 0xFF;     //canal vert
        int b= px & 0xFF;           //canal bleu

        double moy= r*0.21+g*0.72+b*0.07;  //on réalise la moyenne pondérée
        return (byte)moy; //on récupère le pixel grisé
    }

    /**
     *Méthode dont le but est d'enregistrer l'image sous son nouveau nom composé du
     * nom d'origine suivi du traitement effectué à l'image.
     * @param traitement : correspond au traitement effectué sur l'image
     */
    @Override
    public void enregistrementImage(String traitement) {
        //On récupère le nom du fichier
        String[] name = this.img.fn.split("\\.");

        DataBufferInt dataBuffer = new DataBufferInt(this.img.pixelsColored, this.img.pixelsColored.length);
        int samplesPerPixel = 4; // car 4 canaux : aRGB

        //Création du color model où chaque pixel est en 32 bits
        ColorModel colorModel = new DirectColorModel(32,0xFF0000,0xFF00,0xFF,0xFF000000);

        //Création d'un Raster avec les vrais dimensions et mask pour chaque canal.
        WritableRaster raster = Raster.createPackedRaster(dataBuffer, IMG_WIDTH, IMG_HEIGHT, IMG_WIDTH,((DirectColorModel) colorModel).getMasks(), null);
        BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
        RenderedOp op1 = JAI.create("filestore", image, name[0]+"-"+traitement+".png", "png");
    }
    /**
     * Méthode dont le but est de créer une instance de la classe TraitementImageColored et d'enregistrer
     * l'image à traiter.
     * @param img fichier à traiter
     * @param traitement traitement demandé
     */
    @Override
    public void lancerTraitement(Image img, String traitement){
        TraitementImageColored t= new TraitementImageColored(img);
        t.enregistrementImage(traitement);
        System.out.println("Le traitement suivant : "+ traitement+ " a été effectué avec succès!");
    }

    @Override
    public void menu(){
        System.out.println("Veuillez sélectionner l'opération que vous voulez effectuer : ");
        System.out.println("1- Transformation en niveau de gris");
        System.out.println("5- Quitter le programme");
        System.out.println("6- Valider les choix déjà selectionnés");
    }
    @Override
    public void lancerTraitementCG(String[] s, Image img){
        TraitementImageColored t= new TraitementImageColored(img);
        for(String traitement : s) {
            if (traitement.equals("transformation")) {
                Image newImg = t.griser();
                t.lancerTraitement(newImg, traitement);
            } else{
                System.out.println("Erreur vous n'avez pas sélectionné un traitement valide pour cette image");
            }
        }
    }
}
