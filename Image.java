package mam3.ipa.projet;

public class Image {

    protected String fn; //fn pour filename
    protected boolean isColored;  //boolean pour savoir si l'image est en couleur
    protected byte[] pixelsGrey; //Tableau de pixels d'une image grise
    protected int[] pixelsColored; //Tableau de pixels d'une image colorée

    /**
     * Constructeur pour une image colorée.
     * @param px tableau d'entiers stockant les pixels d'une image colorée
     * @param fn nom du fichier de l'image créée.
     */
    Image(int[] px, String fn){
        this.isColored=true;
        this.pixelsColored=px;
        this.pixelsGrey=null;
        this.fn=fn;
    }
    /**
     * Constructeur pour une image en nuance de gris.
     * @param px tableau d'entiers stockant les pixels d'une image grise (en byte)
     * @param fn nom du fichier de l'image créée.
     */
    Image(byte[] px, String fn){
        this.isColored=false;
        this.pixelsGrey=px;
        this.pixelsColored=null;
        this.fn=fn;
    }

    /**
     * Si l'image est colorée retourne true, sinon retourne false.
     * @return un boolean correspondant au type de l'image
     */
    public boolean getType(){
        return this.isColored;
    }



}
