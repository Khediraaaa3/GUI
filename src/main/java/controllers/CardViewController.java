package controllers;

import com.jfoenix.controls.JFXButton;
import entities.Materiel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import services.MaterielService;

import java.io.IOException;
import java.util.Optional;

public class CardViewController {

    @FXML private ImageView imageView;
    @FXML private Label nomLabel, typeLabel, qteTotLabel, qteDispLabel, idLieuLabel, idFournLabel;
    @FXML private VBox detailsContainer;
    @FXML private JFXButton detailsButton;
    @FXML private AnchorPane cardRoot;

    private Materiel materiel;
    private Cards parentController; // ✅ On utilise maintenant l'interface 'Cards'

    public void setData(Materiel m) {
        this.materiel = m;

        try {
            Image image = new Image("/org/example/demo/mate.png");
            if (image.isError()) {
                System.err.println("🚨 Erreur : mate.png introuvable !");
                imageView.setImage(null); // Ne pas afficher d'image si problème
            } else {
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("❌ Impossible de charger l'image.");
            imageView.setImage(null);
        }

        nomLabel.setText("Nom : " + m.getNom_mat());
        typeLabel.setText("Type : " + m.getType_mat());
        qteTotLabel.setText("Qté totale : " + m.getQte_tot());
        qteDispLabel.setText("Qté disp. : " + m.getQte_disp());
        idLieuLabel.setText("ID Lieu : " + m.getIdLieu());
        idFournLabel.setText("ID Fournisseur : " + m.getId_fourn());
    }

    public void setParentController(Cards controller) {
        this.parentController = controller;
    }

    @FXML
    private void modifier() {
        try {
            // ✅ Chemin absolu depuis resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifMateriel.fxml"));

            // ✅ Vérification avant chargement
            if (loader.getLocation() == null) {
                System.err.println("❌ Erreur : fichier FXML introuvable !");
                return;
            }

            Pane root = loader.load();

            ModifMaterielController modifController = loader.getController();
            modifController.initData(materiel);
            modifController.setParentController(parentController);

            Stage stage = new Stage();
            stage.setTitle("Modifier Matériel");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur", "Impossible de charger l'interface de modification.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void supprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer Matériel");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer " + materiel.getNom_mat() + " ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            boolean success = new MaterielService().supprimerM(materiel.getId_mat());

            if (success && parentController != null) {
                parentController.reloadData(); // ✅ Appel via l'interface Cards
            }
        }
    }

    @FXML
    private void showDetails() {
        boolean wasVisible = detailsContainer.isVisible();
        detailsContainer.setVisible(!wasVisible);
        detailsContainer.setManaged(!wasVisible);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), cardRoot);
        scale.setToY(wasVisible ? 1.0 : 1.3);
        scale.play();
    }
}