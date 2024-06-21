package Model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class VoitureUser extends Voiture {
    private String couleur;
    private String motif;
    private String model;
    private String imagePath;
    //constructeur
    public VoitureUser(int width, int height, int initialX, int initialY, int movementVariable, String imagePath) {
        super(width, height, initialX, initialY, movementVariable, imagePath);
        this.couleur = "jaune";
        this.motif = null;
        this.model = "Classique";
        this.imagePath = imagePath;
    }

    public void setPath(String path) {
        this.imagePath = path;
        setImageRectangle(path);
    }

    public String getPath() {
        return this.imagePath;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
        updateImage();
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
        updateImage();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        updateImage();
    }
    // méthode pour update les modifications faites au niveau de la personnalisation de voiture
    private void updateImage() {
        StringBuilder imagePathBuilder = new StringBuilder("/com/example/projetihmjeu/images/yo"); //on utilise SceneBuilder car c'est plus efficace en termes de mémoire
        if (model != null && !"Classique".equals(model)) {
            imagePathBuilder.append("_model");
        }
        if (couleur != null) {
            imagePathBuilder.append("_").append(couleur);
        }
        if (motif != null) {
            imagePathBuilder.append("_").append(motif);
        }
        imagePathBuilder.append(".png");
        String imagePath = imagePathBuilder.toString();

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                System.err.println("Error loading image from path: " + imagePath);
            } else {
                this.rectangle.setFill(new ImagePattern(image));
                this.imagePath = imagePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception loading image from path: " + imagePath);
        }
    }

    @Override
    public void move() {
    }
}