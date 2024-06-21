package com.example.projetihmjeu;

import Model.Leaderboard;
import Model.Rank;
import Model.VoitureUser;
import Model.VoitureObstacle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Random;

public class GameController implements Initializable {

    private VoitureUser voiture_utilisateur;
    private List<VoitureObstacle> obstacles = new ArrayList<>();
    private List<Integer> predefinedXPositions = Arrays.asList(312, 312 + 230, 312 + 230 * 2, 312 + 230 * 3);
    private static final int OBSTACLE_Y_INCREMENT = 300;
    private static final int MAX_OBSTACLES_PER_LINE = 3;
    private Random random = new Random();
    private boolean gameOver = false;
    private int score = 0;
    public static Stage gameStage;

    public void setGameStage(Stage stage) {
        gameStage = stage;
        gameStage.setResizable(false); // c'est pour interdire le redissionnement du fenetre du jeu
        stage.setMaximized(true);
    }

    @FXML
    private AnchorPane scene;

    @FXML
    private TextField txtFieldScore;


    @FXML
    public void start(ActionEvent event) {   // démarrer le jeu et positionner la voiture de l'utilisateur
        voiture_utilisateur.getRectangle().setLayoutY(250);
        voiture_utilisateur.getRectangle().setLayoutX(250);
        resetGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //ici on initialise le controller
        voiture_utilisateur = PrincipalController.utilisateurConnecte.getVoitureUser();
        scene.getChildren().add(voiture_utilisateur.getRectangle());
        scene.getChildren().add(voiture_utilisateur.getHitbox());

        mouvement();
        updateScore();
    }

    private void generation_obstacle() { //génèrer aléatoirement les obstacles qui sont des voitures aussi, et les ajouter à la scene
        int yPosition = -OBSTACLE_Y_INCREMENT;
        List<Integer> availablePositions = new ArrayList<>(predefinedXPositions);
        int obstaclesInWave = random.nextInt(MAX_OBSTACLES_PER_LINE) + 1;
        for (int i = 0; i < obstaclesInWave; i++) {
            int xIndex = random.nextInt(availablePositions.size());
            int xPosition = availablePositions.remove(xIndex);
            VoitureObstacle obstacle = VoitureObstacle.createObstacle(225, 200, 10, "/images/1.png", xPosition, yPosition);
            obstacles.add(obstacle);
            scene.getChildren().add(obstacle.getRectangle());
            scene.getChildren().add(obstacle.getHitbox());
        }
    }

    public void mouvement() {
        scene.setOnKeyPressed(e -> {
            if (!gameOver) {
                if (e.getCode() == KeyCode.Q) {
                    voiture_utilisateur.moveLeft();
                }
                if (e.getCode() == KeyCode.D) {
                    voiture_utilisateur.moveRight();
                }
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                boolean needNewWave = true;
                for (VoitureObstacle obstacle : obstacles) {
                    obstacle.move(); //bouger l'obstacle vers le bas //
                    if (checkCollision(voiture_utilisateur, obstacle)) {
                        this.stop();
                        gameOver = true;
                        Platform.runLater(() -> message_alert("Collision", "Vous avez perdu !"));
                        return;
                    }
                    if (obstacle.getRectangle().getLayoutY() < OBSTACLE_Y_INCREMENT) {
                        needNewWave = false;
                    }
                } // needNewWave permet de savoir s'il ya besoin de générer des nouveaux obstacles ou pas //
                if (needNewWave) {
                    generation_obstacle();
                }
                if (!gameOver) {
                    score++;
                    updateScore();
                }
            }
        }.start();
    }

    private boolean checkCollision(VoitureUser user, VoitureObstacle obstacle) { //vérifier la collision entre la voiture de user et les obstacles//
        return user.getHitbox().getBoundsInParent().intersects(obstacle.getHitbox().getBoundsInParent());
    }

    private void message_alert(String title, String message) { // cette fonction nous permet d'afficher le message d'alerte en cas de collision
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(response -> {
            if (PrincipalController.utilisateurConnecte != null) {
                Leaderboard.addScore(PrincipalController.utilisateurConnecte.getUsername(), score); //mettre à jour le score dans le Leaderboard
                int meilleur = PrincipalController.utilisateurConnecte.getMeilleurScore();
                if(score > meilleur){
                    PrincipalController.utilisateurConnecte.setMeilleurScore(score);
                    if(score > 750){
                        PrincipalController.utilisateurConnecte.setUserRank(Rank.Or);
                    }
                    if(score > 1500){
                        PrincipalController.utilisateurConnecte.setUserRank(Rank.Platine);
                    }
                    if(score > 2000){
                        PrincipalController.utilisateurConnecte.setUserRank(Rank.Champion);
                    }
                }
            }
            if (gameStage != null) {
                gameStage.close();
            }
            Platform.runLater(() -> {
                PrincipalController principalController = new PrincipalController();
                principalController.showMainMenu();
            });
        });
    }

    private void updateScore() {
        txtFieldScore.setText(String.valueOf(score));
    }

    private void resetGame() { //méthode pour réinitialiser le jeu
        score = 0;
        gameOver = false;
        obstacles.clear();
        scene.getChildren().clear();
        scene.getChildren().add(voiture_utilisateur.getRectangle());
        scene.getChildren().add(voiture_utilisateur.getHitbox());
        updateScore();
        generation_obstacle();
    }
}