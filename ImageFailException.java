package mam3.ipa.projet;

public class ImageFailException extends Exception {
    private String fn;
    public ImageFailException(String fn){
        this.fn=fn;
    }
    public String toString(){
        return "error "+ this.fn + " is not an image";
    }
}
