package com.example.projetihmjeu;

import Model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PrincipalController {
    public static ArrayList<Utilisateur> listeInscrits = new ArrayList<>(); // Stocke les utilisateurs inscrits
    public static Utilisateur utilisateurConnecte; // Permet d'accéder à l'utilisateur qui est connecté
    public static Stage accueilStage;
    public static Stage connexionStage;
    public static Stage inscriptionStage;
    public static Stage ajoutAmiStage;
    public static Stage listeAmisStage;
    public static Stage retraitAmiStage;


    @FXML
    private TextField txtFieldUsername1; // TextField de la fenêtre Connexion (pour stocker ce que l'utilisateur met)

    @FXML
    private TextField txtFieldUsername; // TextField de la fenêtre Inscription (pour stocker ce que l'utilisateur met)

    @FXML
    private TextField txtFieldAjoutAmi; // TextField de la fenêtre AjoutAmis (pour stocker ce que l'utilisateur met)

    @FXML
    private ListView listViewAmis; // Permet d'afficher la liste d'amis d'un utilisateur

    @FXML
    private TextField txtfieldRetirerAmi; // TextField de la fenêtre RetirerAmi (pour stocker ce que l'utilisateur met)

    @FXML
    Label labelMeilleurScore;

    @FXML
    private Label username; // Affiche l'username de l'utilisateur connecté dans le menu principal

    @FXML
    private Label rank; // Affiche le rank de l'utilisateur connecté dans le menu principal

    @FXML
    private Label meilleurScore; // Affiche le meilleur de l'utilisateur connecté dans le menu principal

    public void setConnexionStage(Stage stage) {
        connexionStage = stage;
    }

    public void setInscriptionStage(Stage stage) {
        inscriptionStage = stage;
    }

    public void setAjoutAmiStage(Stage stage){
        ajoutAmiStage = stage;
    }

    public void setListeAmisStage(Stage stage){
        listeAmisStage = stage;
    }

    public void setRetraitAmiStage(Stage stage){
        retraitAmiStage = stage;
    }


    // Actualise le meilleur score de l'utilisateur connecté à chaque fois qu'on revient au menu principal
    public void updateLabelMeilleurScore() {
        if (meilleurScore != null && utilisateurConnecte != null) {
            meilleurScore.setText("" + utilisateurConnecte.getMeilleurScore());
        }
    }

    // Actualise l'username de l'utilisateur connecté à chaque fois qu'on revient au menu principal
    public void updateLabelUsername() {
        if (username != null && utilisateurConnecte != null) {
            username.setText(utilisateurConnecte.getUsername());
        }
    }

    // Actualise le rank de l'utilisateur connecté à chaque fois qu'on revient au menu principal
    public void updateLabelRank() {
        if (rank != null && utilisateurConnecte != null) {
            rank.setText(utilisateurConnecte.getUserRank().name());
        }
    }

    // Permet de revenir au menu principal à partir d'autres classes comme gameController par exemple (tout en actualisant les informations)
    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.setResizable(false);
            stage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Désinscrit l'utilisateur actuellement connencté
    @FXML
    private void handlebtnDesinscrire(ActionEvent event) {
        try {
            listeInscrits.remove(utilisateurConnecte);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAccueil.fxml"));
            Parent root = loader.load();

            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();

            Stage stage = new Stage();
            stage.setTitle("Accueil");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gère le retrait d'ami
    @FXML
    private void handlebtnValiderRetraitAmi(ActionEvent event) {
        String username = txtfieldRetirerAmi.getText();
        if (username != null && !username.trim().isEmpty()) {
            retirerAmi(username);
            if (retraitAmiStage != null) {
                retraitAmiStage.close();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
                Parent root = loader.load();

                accueilStage = new Stage();
                accueilStage.setScene(new Scene(root));
                accueilStage.setTitle("Menu Principal");
                accueilStage.setResizable(false);
                accueilStage.show();
                PrincipalController controller = loader.getController();
                controller.updateLabelUsername();
                controller.updateLabelRank();
                controller.updateLabelMeilleurScore();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message_alert(Alert.AlertType.WARNING, "Nom d'utilisateur vide", "Le nom d'utilisateur ne peut pas être vide");
            System.out.println("Le nom d'utilisateur ne peut pas être vide");
        }
    }

    // Revenir de la fenêtre "Retirer Ami" à la fenêtre "Menu principal" sans retirer d'amis
    @FXML
    private void handlebtnRetourRetirerAmi(ActionEvent event) {
        if (retraitAmiStage != null) {
            retraitAmiStage.close();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Parent root = loader.load();
            accueilStage = new Stage();
            accueilStage.setScene(new Scene(root));
            accueilStage.setTitle("Accueil");
            accueilStage.setResizable(false);
            accueilStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Revenir de la fenêtre "Liste Amis" à la fenêtre "Menu principal"
    @FXML
    private void handlebtnRetourListeAmis(ActionEvent event) {
        if (listeAmisStage != null) {
            listeAmisStage.close();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Parent root = loader.load();
            accueilStage = new Stage();
            accueilStage.setScene(new Scene(root));
            accueilStage.setTitle("Accueil");
            accueilStage.setResizable(false);
            accueilStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un ami à la liste d'amis de l'utilisateur connecté (en prenant la valeur stockée dans le textfield)
    @FXML
    private void handlebtnValiderAjoutAmi(ActionEvent event) {
        String username = txtFieldAjoutAmi.getText();
        if (username != null && !username.trim().isEmpty()) {
            ajouterAmi(username);
            if (ajoutAmiStage != null) {
                ajoutAmiStage.close();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
                Parent root = loader.load();
                accueilStage = new Stage();
                accueilStage.setScene(new Scene(root));
                accueilStage.setTitle("Menu Principal");
                accueilStage.setResizable(false);
                accueilStage.show();
                PrincipalController controller = loader.getController();
                controller.updateLabelUsername();
                controller.updateLabelRank();
                controller.updateLabelMeilleurScore();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message_alert(Alert.AlertType.WARNING, "Nom d'utilisateur vide", "Le nom d'utilisateur ne peut pas être vide");
            System.out.println("Le nom d'utilisateur ne peut pas être vide");
        }
    }

    // Revenir de la fenêtre "Ajouter Ami" à la fenêtre "Menu principal" sans ajouter d'amis
    @FXML
    private void HandlebtnRetourAjoutAmi(ActionEvent event) {
        if (ajoutAmiStage != null) {
            ajoutAmiStage.close();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
            Parent root = loader.load();
            accueilStage = new Stage();
            accueilStage.setScene(new Scene(root));
            accueilStage.setTitle("Accueil");
            accueilStage.setResizable(false);
            accueilStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permet à l'utilisateur de se connecter (en utilisant l'username mit dans le textField)
    @FXML
    private void handlebtnValider1(ActionEvent event) {
        String username = txtFieldUsername1.getText();
        if (username != null && !username.trim().isEmpty()) {
            try {
                if(connexion(username)){
                    utilisateurConnecte = getInscrit(username);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
                    Parent root = loader.load();

                    PrincipalController controller = loader.getController();
                    controller.updateLabelUsername();
                    controller.updateLabelRank();
                    controller.updateLabelMeilleurScore();
                    if (connexionStage != null) {
                        connexionStage.close();
                    }
                    if (accueilStage != null) {
                        accueilStage.close();
                    }
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Menu Principal");
                    stage.setResizable(false);
                    stage.show();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message_alert(Alert.AlertType.WARNING, "Nom d'utilisateur vide", "Le nom d'utilisateur ne peut pas être vide");
            System.out.println("Le nom d'utilisateur ne peut pas être vide");
        }
    }

    // Revenir de la fenêtre "Connexion" à la fenêtre "Menu Accueil" sans se connecter
    @FXML
    private void handlebtnRetour1(ActionEvent event) {
        if (connexionStage != null) {
            connexionStage.close();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAccueil.fxml"));
            Parent root = loader.load();
            accueilStage = new Stage();
            accueilStage.setScene(new Scene(root));
            accueilStage.setTitle("Accueil");
            accueilStage.setResizable(false);
            accueilStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permet à l'utilisateur de s'inscrire (en utilisant l'username mit dans le textField)
    @FXML
    private void handlebtnValider(ActionEvent event) {
        String username = txtFieldUsername.getText();
        if (username != null && !username.trim().isEmpty()) {
            ajouterInscrit(username);
            if (inscriptionStage != null) {
                inscriptionStage.close();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAccueil.fxml"));
                Parent root = loader.load();
                Stage accueilStage = new Stage();
                accueilStage.setScene(new Scene(root));
                accueilStage.setTitle("Accueil");
                accueilStage.setResizable(false);
                accueilStage.show();
                PrincipalController controller = loader.getController();
                controller.updateLabelUsername();
                controller.updateLabelRank();
                controller.updateLabelMeilleurScore();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message_alert(Alert.AlertType.WARNING, "Nom d'utilisateur vide", "Le nom d'utilisateur ne peut pas être vide");
            System.out.println("Le nom d'utilisateur ne peut pas être vide");

        }
    }

    // Revenir de la fenêtre "Inscrire" à la fenêtre "Menu Accueil" sans s'inscrire
    @FXML
    private void handlebtnRetour(ActionEvent event) {
        if (inscriptionStage != null) {
            inscriptionStage.close();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAccueil.fxml"));
            Parent root = loader.load();
            accueilStage = new Stage();
            accueilStage.setScene(new Scene(root));
            accueilStage.setTitle("Accueil");
            accueilStage.setResizable(false);
            accueilStage.show();
            PrincipalController controller = loader.getController();
            controller.updateLabelUsername();
            controller.updateLabelRank();
            controller.updateLabelMeilleurScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre leaderboard
    @FXML
    private void handlebtnLeaderboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuLeaderboard.fxml"));
            Parent root = loader.load();
            LeaderboardController leaderboardController = loader.getController();
            Stage leaderboardStage = new Stage();
            leaderboardController.setLeaderboardStage(leaderboardStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            leaderboardStage.setTitle("Jeu de Voiture");
            leaderboardStage.setScene(new Scene(root));
            leaderboardStage.setResizable(true);
            leaderboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            message_alert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de jeu.");
        }
    }

    // Affiche la fenêtre "listeAmis"
    @FXML
    private void handlebtnlisteAmis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAmis.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            controller.setListeAmisStage(listeAmisStage);
            Stage listeAmisStage = new Stage();
            controller.setListeAmisStage(listeAmisStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            listeAmisStage.setTitle("Liste d'amis");
            listeAmisStage.setScene(new Scene(root));
            listeAmisStage.setResizable(false);
            listeAmisStage.show();
            if (controller.listViewAmis != null) {
                ObservableList<String> items = FXCollections.observableArrayList();
                for (Utilisateur ami : utilisateurConnecte.getListeAmis()) {
                    items.add(ami.getUsername());
                }
                controller.listViewAmis.setItems(items);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre "Ajouter Ami"
    @FXML
    private void handlebtnajoutAmi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAjoutAmi.fxml"));
            Parent root = loader.load();

            PrincipalController controller = loader.getController();
            Stage ajoutAmiStage = new Stage();
            controller.setAjoutAmiStage(ajoutAmiStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            ajoutAmiStage.setTitle("Ajouter un ami");
            ajoutAmiStage.setScene(new Scene(root));
            ajoutAmiStage.setResizable(false);
            ajoutAmiStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre (Retirer Ami)
    @FXML
    private void handlebtnretirerAmi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuRetraitAmi.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            Stage retraitAmiStage = new Stage();
            controller.setRetraitAmiStage(retraitAmiStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            retraitAmiStage.setTitle("Retirer un ami");
            retraitAmiStage.setScene(new Scene(root));
            retraitAmiStage.setResizable(false);
            retraitAmiStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permet à l'utilisateur acutuellement connecté de se déconnecter et donc revenir au menu d'Accueil
    @FXML
    private void handlebtnSignOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuAccueil.fxml"));
            Parent root = loader.load();
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            Stage stage = new Stage();
            stage.setTitle("Accueil");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre d'inscription
    @FXML
    private void handlebtnInscription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuInscription.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            Stage inscriptionStage = new Stage();
            controller.setInscriptionStage(inscriptionStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            inscriptionStage.setTitle("Inscription");
            inscriptionStage.setScene(new Scene(root));
            inscriptionStage.setResizable(false);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre de connexion
    @FXML
    private void handlebtnConnexion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuConnexion.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            Stage connexionStage = new Stage();
            controller.setConnexionStage(connexionStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            connexionStage.setTitle("Connexion");
            connexionStage.setScene(new Scene(root));
            connexionStage.setResizable(false);
            connexionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche la fenêtre de jeu et fait appel au GameController
    @FXML
    private void handlebtnJouer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
            Parent root = loader.load();
            GameController gameController = loader.getController();
            Stage gameStage = new Stage();
            gameController.setGameStage(gameStage);
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            gameStage.setTitle("Jeu de Voiture");
            gameStage.setScene(new Scene(root));
            gameStage.setResizable(false);
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            message_alert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de jeu.");
        }
    }

    // Affiche la fenêtre de personnalisation
    @FXML
    private void handlebtnVoiture(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalisation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Stage mainStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            mainStage.close();
            stage.setTitle("Vehicle Personalisation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permet de générer des alertes (par exemple si un username est déjà pris pour une inscription)
    private void message_alert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Permet à un utilisateur de se connecter mais également de générer une alerte si l'username n'existe pas par exemple
    public boolean connexion(String u) {
        try {
            for (Utilisateur utilisateur : listeInscrits) {
                if (utilisateur.getUsername().equals(u)) {
                    System.out.println("Connexion réussie");
                    return true;
                }
            }
            message_alert(Alert.AlertType.ERROR, "Erreur de connexion", "Cet utilisateur n'existe pas");
            throw new RuntimeException("Cet utilisateur n'existe pas");
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Vérifie si un utilisateur est déjà inscrit
    public boolean verifierInscrit(String u) {
        for (Utilisateur utilisateur : listeInscrits) {
            if (utilisateur.getUsername().equals(u)) {
                return true;
            }
        }
        return false;
    }

    // Permet d'ajouter un inscrit à la liste des inscrits (qui est stockée dans le controller)
    public void ajouterInscrit(String u) {
        try{
            if (!verifierInscrit(u)) {
                Utilisateur utilisateur = new Utilisateur(u);
                listeInscrits.add(utilisateur);
                System.out.println("Vous avez été inscrit");
            } else {
                throw new RuntimeException("Username déjà pris");
            }
        }
        catch(RuntimeException e){
            message_alert(Alert.AlertType.WARNING, "Nom d'utilisateur déjà pris", "Le nom d'utilisateur a déjà été pris");
            System.out.println(e.getMessage());
        }
    }

    // Permet de get un objet utilisateur parmi ceux qui sont inscrits
    public Utilisateur getInscrit(String u){
        for(Utilisateur utilisateur : listeInscrits){
            if(utilisateur.getUsername().equals(u)){
                return utilisateur;
            }
        }
        return null;
    }

    // Vérifie si un utilisateur est ami avec l'utilisateur actuellement connecté
    public boolean verifierAmi(Utilisateur utilisateur){
        for(Utilisateur u: utilisateurConnecte.getListeAmis()){
            if(u.getUsername().equals(utilisateur.getUsername())){
                return true;
            }
        }
        return false;
    }

    // Ajoute un ami
    public void ajouterAmi(String u){
        try{
            if (verifierInscrit(u)){
                Utilisateur ami = getInscrit(u);
                if(!(ami.getUsername().equals(utilisateurConnecte.getUsername())) && !(verifierAmi(ami))){
                    utilisateurConnecte.addListeAmis(ami);
                    System.out.println(" Ami ajouté avec succès");
                }
                else{
                    message_alert(Alert.AlertType.WARNING, "Erreur ajout d'ami", "Vous ne pouvez pas ajouter cet utilisateur");
                    throw new RuntimeException("Vous ne pouvez pas ajouter cet utilisateur");
                }

            }
            else {
                message_alert(Alert.AlertType.WARNING, "Erreur ajout d'ami", "Cet utilisateur n'existe pas");
                throw new RuntimeException("Cet utilisateur n'existe pas");
            }
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    // Retire un ami
    public void retirerAmi(String u){
        try{
            if (verifierInscrit(u)){
                Utilisateur ami = getInscrit(u);
                if(!(ami.getUsername().equals(utilisateurConnecte.getUsername())) && verifierAmi(ami)){
                    utilisateurConnecte.removeListeAmis(ami);
                    System.out.println(" Ami retiré avec succès");
                }
                else{
                    message_alert(Alert.AlertType.WARNING, "Erreur de retrait d'ami", "Vous ne pouvez pas retirer cet utilisateur");
                    throw new RuntimeException("Vous ne pouvez pas retirer cet utilisateur");
                }
            }
            else {
                message_alert(Alert.AlertType.WARNING, "Erreur de retrait d'ami", "Cet utilisateur n'est pas dans votre liste d'amis");
                throw new RuntimeException("Cet utilisateur n'est pas dans votre liste d'amis");
            }
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }
}