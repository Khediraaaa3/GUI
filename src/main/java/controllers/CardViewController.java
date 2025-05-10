package controllers;

import com.jfoenix.controls.JFXButton;
import entities.Materiel;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class CardViewController {

    @FXML private ImageView imageView;
    @FXML private Label nomLabel, typeLabel, qteTotLabel, qteDispLabel, idLieuLabel, idFournLabel;
    @FXML private VBox detailsContainer;
    @FXML private JFXButton detailsButton;

    @FXML private AnchorPane cardRoot;

    private Materiel materiel;
    private CardsController parentController;

    public void setData(Materiel m) {
        this.materiel = m;

        // Charger l'image
        Image image = new Image(getClass().getResourceAsStream("/org/example/demo/mate.png"));
        imageView.setImage(image);

        // Infos principales
        nomLabel.setText("Nom : " + m.getNom_mat());
        typeLabel.setText("Type : " + m.getType_mat());

        // Infos détaillées
        qteTotLabel.setText("Qté totale : " + m.getQte_tot());
        qteDispLabel.setText("Qté disp. : " + m.getQte_disp());
        idLieuLabel.setText("ID Lieu : " + m.getIdLieu());
        idFournLabel.setText("ID Fournisseur : " + m.getId_fourn());
    }

    public void setParentController(CardsController controller) {
        this.parentController = controller;
    }

    @FXML
    private void showDetails() {
        boolean wasVisible = detailsContainer.isVisible();

        // Bascule la visibilité
        detailsContainer.setVisible(!wasVisible);
        detailsContainer.setManaged(!wasVisible);

        // Animation optionnelle : agrandir la carte
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), cardRoot);
        scale.setToY(wasVisible ? 1.0 : 1.3); // Agrandir légèrement
        scale.play();
    }

    @FXML
    private void modifier() {
        try {
            // ✅ Charger le FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifMateriel.fxml"));
            VBox root = loader.load();

            // ✅ Récupérer le contrôleur et lui passer le matériel
            ModifMaterielController modifController = loader.getController();
            modifController.initData(materiel); // Remplir les champs avec les données actuelles

            // ✅ Passer une référence du contrôleur principal pour recharger après modification
            modifController.setParentController(parentController);

            // ✅ Ouvrir une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier Matériel");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimer() {
        System.out.println("Supprimer matériel : " + materiel.getNom_mat());
        // Code pour supprimer
    }
}