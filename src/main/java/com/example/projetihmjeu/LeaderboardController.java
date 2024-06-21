package com.example.projetihmjeu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Model.Leaderboard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderboardController {
    public static Stage leaderboardStage;

    @FXML
    private ListView<String> listViewScores;

    public void setLeaderboardStage(Stage stage){
        leaderboardStage = stage;
    }

    @FXML
    public void initialize() {
        for (Leaderboard.ScoreEntry entry : Leaderboard.getScores()) {
            listViewScores.getItems().add(entry.getUsername() + ": " + entry.getScore());
        }
    }

    @FXML
    private void handlebtnRetourLeaderboard(ActionEvent event) {
        if (leaderboardStage != null) {
            leaderboardStage.close();
        }
        Stage Stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Parent root = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle("Menu Principal");
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);
            mainStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
