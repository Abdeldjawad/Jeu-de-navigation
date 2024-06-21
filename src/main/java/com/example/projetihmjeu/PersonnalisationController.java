package com.example.projetihmjeu;

import Model.VoitureUser;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;

public class PersonnalisationController {
    private String couleur_actuelle;
    private String motif_selectionne;
    private String model_selectionne;
    public static Stage personnalisationStage;
    private static final String[] COULEURS = {"vert", "bleu", "jaune", "rouge"};
    private static final String[] MOTIFS = {"flame", null};
    @FXML
    private ComboBox<String> modelSelector;

    @FXML
    private Pane vehiclePane;

    @FXML
    private ImageView carBody;

    @FXML
    private void handleModelSelection() {
        model_selectionne = modelSelector.getSelectionModel().getSelectedItem(); // utilisateur sélectionne un modèle
        updateCarImage(); // mettre à jour le choix que l'utilisateur a fait
    }
    @FXML
    private void handlebtnValider(ActionEvent event) {
        VoitureUser voitureUser = PrincipalController.utilisateurConnecte.getVoitureUser();
        voitureUser.setCouleur(couleur_actuelle);
        voitureUser.setMotif(motif_selectionne);
        voitureUser.setModel(model_selectionne); // sauvegarde les modifications de la voiture utilisateur
        if (personnalisationStage != null) {
            personnalisationStage.close();
        }
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        PrincipalController principalController = new PrincipalController();
        principalController.showMainMenu();
    } // lorsqu'on appuie sur le bouton valider cette méthode est appelé et va sauvegardé la nouvelle voiture dans le jeu


    @FXML
    private void initialize() {
        modelSelector.setItems(FXCollections.observableArrayList("Classique", "Spécial"));
        VoitureUser voiture_utilisateur = PrincipalController.utilisateurConnecte.getVoitureUser();
        couleur_actuelle = voiture_utilisateur.getCouleur();
        motif_selectionne = voiture_utilisateur.getMotif();
        model_selectionne = voiture_utilisateur.getModel();

        // met l'objet dans ComboBox
        modelSelector.getSelectionModel().select(model_selectionne);

        updateCarImage();
        vehiclePane.setStyle("-fx-alignment: center;");
    }

    @FXML
    private void handleRandomSelection() {
        Random random = new Random();
        couleur_actuelle = COULEURS[random.nextInt(COULEURS.length)];
        motif_selectionne = MOTIFS[random.nextInt(MOTIFS.length)];
        model_selectionne = modelSelector.getItems().get(random.nextInt(modelSelector.getItems().size()));

        modelSelector.getSelectionModel().select(model_selectionne);

        updateCarImage();
    }
    @FXML
    private void handleDragDetected(javafx.scene.input.MouseEvent evenement_souris) {
        Dragboard db = ((javafx.scene.Node) evenement_souris.getSource()).startDragAndDrop(TransferMode.COPY); // commence une opération de drag and drop avec le mode de transfert en copie.
        ClipboardContent contenu_obj = new ClipboardContent(); // Crée un nouvel objet ClipboardContent pour stocker les données de l'élément à drag and drop.
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT); //Définit les paramètres de capture d'écran pour rendre l'arrière-plan transparent.
        if (evenement_souris.getSource() instanceof Circle) {
            Circle source = (Circle) evenement_souris.getSource();
            contenu_obj.putString(source.getFill().toString());
            db.setDragView(source.snapshot(snapshotParameters, null));
        } else if (evenement_souris.getSource() instanceof ImageView) {
            ImageView source = (ImageView) evenement_souris.getSource();
            contenu_obj.putString("flameMotif");
            db.setDragView(source.snapshot(snapshotParameters, null));
        }
        db.setContent(contenu_obj);
        evenement_souris.consume();
    }
   // Cette méthode est déclenchée lorsque l'utilisateur commence à faire glisser soit une couleur(cercle) soit le motif

    @FXML
    private void handleDragOver(DragEvent evenement_souris) {
        if (evenement_souris.getGestureSource() != vehiclePane && evenement_souris.getDragboard().hasString()) {
            evenement_souris.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        evenement_souris.consume();
    } //Lorsqu'un élément glissé est déplacé au-dessus de la zone cible (ici, le vehiclePane), l'événement handleDragOver est déclenché.

    @FXML
    private void handleDragDropped(DragEvent evenement_souris) {
        Dragboard db = evenement_souris.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String custom = db.getString();
            String model = modelSelector.getSelectionModel().getSelectedItem();
            if (custom.equals("flameMotif")) {
                success = handleMotifDrop(model, "flame");
                motif_selectionne = "flame";
            } else {
                String couleur = custom;
                if ("0x32cd32ff".equals(couleur)) {
                    couleur_actuelle = "vert";
                } else if ("0x0000ffff".equals(couleur)) {
                    couleur_actuelle = "bleu";
                } else if ("0xffff00ff".equals(couleur)) {
                    couleur_actuelle = "jaune";
                } else if ("0xff0000ff".equals(couleur)) {
                    couleur_actuelle = "rouge";
                }
                motif_selectionne = null;
                success = handleColorDrop(model, couleur_actuelle);
            }
        }
        evenement_souris.setDropCompleted(success);
        evenement_souris.consume();
    }//  Lorsque l'utilisateur lâche l'élément dans la zone cible, ça met à jour l'affichage de la voiture.

    private boolean handleColorDrop(String model, String color) {
        String nom_image = getImage(model, color, null);
        URL imageUrl = getClass().getResource("/com/example/projetihmjeu/images/" + nom_image);
        if (imageUrl != null) {
            carBody.setImage(new Image(imageUrl.toExternalForm()));
            return true;
        } else {
            System.err.println("erreur de chargement pour couleur: " + color + "et modèle : " + model);
            return false;
        }
    }

    private boolean handleMotifDrop(String model, String motif) {
        String nom_image = getImage(model, couleur_actuelle, motif);
        URL imageUrl = getClass().getResource("/com/example/projetihmjeu/images/" + nom_image);
        if (imageUrl != null) {
            carBody.setImage(new Image(imageUrl.toExternalForm()));
            return true;
        } else {
            System.err.println("erreur de chargement pour couleur: " + couleur_actuelle + " et modèle: " + model + " avec motif: " + motif);
            return false;
        }
    }
 // Les méthodes handleColorDrop et handleMotifDrop sont utilisées pour mettre à jour l'image de la voiture en fonction de la couleur ou du motif sélectionné.

    private String getImage(String model, String color, String motif) {
        StringBuilder nom_image = new StringBuilder("yo");
        if ("Spécial".equals(model)) {
            nom_image.append("_model");
        }
        nom_image.append("_").append(color);
        if (motif != null) {
            nom_image.append("_").append(motif);
        }
        nom_image.append(".png");
        return nom_image.toString();
    } //  cette foncion lorsuqu'elle est appelé va parcourir et chercher l'image qui correspond au bon modèle et à la bonne couleur

    private void updateCarImage() {
        handleColorDrop(model_selectionne, couleur_actuelle);
        if (motif_selectionne != null) {
            handleMotifDrop(model_selectionne, motif_selectionne);}}
    // va mettre à jour l'image en appelant  handleColorDrop et handleMotifDrop
}
