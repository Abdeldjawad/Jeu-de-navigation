package Model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class Voiture {
    protected Rectangle rectangle; // rectangle dans lequel on va load l'image de la voiture
    protected Rectangle hitbox; // rectangle qui va modéliser la hitbox du véhicule
    protected int movementVariable;

    public Voiture(int width, int height, int initialX, int initialY, int movementVariable, String imagePath) {
        this.rectangle = new Rectangle(width, height);
        this.rectangle.setLayoutX(initialX);
        this.rectangle.setLayoutY(initialY);
        this.movementVariable = movementVariable;

        if (imagePath != null) {
            setImageRectangle(imagePath);
        } else {
            this.rectangle.setFill(Color.BLACK); // partie ajoutée pour débugger (pour voir s'il y a un problème)
        }


        int hitboxWidth = (int) (width * 0.4);  // Gérer la taille de la hitbox
        int hitboxHeight = (int) (height * 0.8);
        this.hitbox = new Rectangle(hitboxWidth, hitboxHeight);
        this.hitbox.setLayoutX(initialX + (width - hitboxWidth) / 2);
        this.hitbox.setLayoutY(initialY + (height - hitboxHeight) / 2);
        this.hitbox.setFill(null);
        this.hitbox.setStroke(Color.TRANSPARENT); // Rendre la hitbox transparente
    }

    // Permet de load l'image de la voiture dans le rectangle
    public void setImageRectangle(String imagePath) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                System.err.println("Error loading image from path: " + imagePath);
            } else {
                this.rectangle.setFill(new ImagePattern(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception loading image from path: " + imagePath);
        }
    }

    // Getter de triangle (pour la classe personnalisation)
    public Rectangle getRectangle() {
        return rectangle;
    }

    // Getter de hitbox (pour game controller)
    public Rectangle getHitbox() {
        return hitbox;
    }

    // Permet de déplacer la voiture vers la gauche
    public void moveLeft() {
        if (rectangle.getLayoutX() > 312) {
            rectangle.setLayoutX(rectangle.getLayoutX() - movementVariable);
            hitbox.setLayoutX(hitbox.getLayoutX() - movementVariable);
        }
    }

    // Permet de déplalacer la voiture vers la droite
    public void moveRight() {
        if (rectangle.getLayoutX() < 978) {
            rectangle.setLayoutX(rectangle.getLayoutX() + movementVariable);
            hitbox.setLayoutX(hitbox.getLayoutX() + movementVariable);
        }
    }

    // Permet de rotate l'image (pour les voitures obstacles)
    public void rotateImage(double angle) {
        this.rectangle.getTransforms().add(new Rotate(angle, rectangle.getWidth() / 2, rectangle.getHeight() / 2));
    }

    public abstract void move();
}