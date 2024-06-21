package Model;

import java.util.List;
import java.util.Random;

public class VoitureObstacle extends Voiture {
    private static final Random random = new Random();
    //constructeur voitutreObstacle
    public VoitureObstacle(int width, int height, int initialX, int initialY, int movementVariable, String imagePath) {
        super(width, height, initialX, initialY, movementVariable, imagePath);
        rotateImage(180); // on fait la rotation de l'image obstacle parce qu'elle descends //
    }

    @Override
    public void move() { //méthode pour déplacer l'obstacle vers le bas
        this.rectangle.setLayoutY(this.rectangle.getLayoutY() + movementVariable); //déplacer la partie graphique de l'obstacle
        this.hitbox.setLayoutY(this.hitbox.getLayoutY() + movementVariable); // déplacer la zone dee collision
    }

    public static VoitureObstacle createObstacle(int width, int height, int movementVariable, String imagePath, int initialX, int initialY) {
        return new VoitureObstacle(width, height, initialX, initialY, movementVariable, imagePath);
    }
}