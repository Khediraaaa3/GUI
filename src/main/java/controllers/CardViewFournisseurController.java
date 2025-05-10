package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.FournisseurService;

import java.io.IOException;
import java.util.Optional;

public class CardViewFournisseurController {

    @FXML private ImageView imageView;
    @FXML private Label nomLabel, numLabel, idLabel;
    @FXML private VBox detailsContainer;
    @FXML private AnchorPane cardRoot;

    private Fournisseur fournisseur;
    private Cards parentController;

    public void setData(Fournisseur f) {
        this.fournisseur = f;

        // ✅ Chargement sécurisé de l'image
        try {
            Image image = new Image(getClass().getResourceAsStream("/org/example/demo/supp.png"));
            if (image.isError()) {
                System.err.println("❌ Erreur : Image supp.png introuvable !");
            } else {
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("🚨 Aucune image trouvée !");
        }

        // Infos principales
        nomLabel.setText("Nom : " + f.getNom_fourn());
        numLabel.setText("Tél : " + f.getNum_fourn());
    }

    public void setParentController(Cards controller) {
        this.parentController = controller;
    }

    @FXML
    private void showDetails() {
        detailsContainer.setVisible(!detailsContainer.isVisible());
        detailsContainer.setManaged(detailsContainer.isVisible());

        // Animation optionnelle
        if (cardRoot != null) {
            double scale = detailsContainer.isVisible() ? 1.3 : 1.0;
            cardRoot.setScaleY(scale);
        }
    }

    @FXML
    private void modifier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifFournisseur.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("❌ Le fichier FXML de modification est introuvable !");
                return;
            }

            Pane root = loader.load();

            ModifFournisseurController modifController = loader.getController();
            modifController.initData(fournisseur);
            modifController.setCardsController(parentController);

            Stage stage = new Stage();
            stage.setTitle("Modifier Fournisseur");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer Fournisseur");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer " + fournisseur.getNom_fourn() + " ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            boolean success = new FournisseurService().supprimerF(fournisseur.getId_fourn());

            if (success && parentController != null) {
                parentController.reloadData(); // ✅ Rafraîchissement via l'interface 'Cards'
            }
        }
    }
}