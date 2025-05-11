package controllers;

import entities.Fournisseur;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.FournisseurService;
import utils.export.ExportUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CardsFournisseurController implements Cards {

    @FXML private FlowPane fournisseursContainer;
    @FXML private Label notificationLabel;

    private final FournisseurService fournisseurService = new FournisseurService();

    public void initialize() {
        if (fournisseursContainer == null) return;

        reloadData();
    }

    @Override
    public void reloadData() {
        List<Fournisseur> liste = new FournisseurService().afficherF(); // Charger les données
        loadCards(liste);
    }

    private void loadCards(List<Fournisseur> fournisseurs) {
        fournisseursContainer.getChildren().clear();

        for (Fournisseur f : fournisseurs) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/CardViewFournisseur.fxml"));
                Pane card = loader.load();

                CardViewFournisseurController controller = loader.getController();
                controller.setData(f);
                controller.setParentController(this);

                fournisseursContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void exportSuppliersToExcel() {
        try {
            List<Fournisseur> fournisseurs = fournisseurService.afficherF();

            // Obtenir la fenêtre actuelle
            Stage currentStage = (Stage) notificationLabel.getScene().getWindow();

            // Utiliser la méthode chooseExportFile
            File selectedFile = chooseExportFile(currentStage, "fournisseurs_export.xlsx");

            if (selectedFile == null) {
                displayNotification("Export annulé.", "orange");
                return;
            }

            ExportUtils.exportFournisseursToExcel(selectedFile.getAbsolutePath(), fournisseurs);
            displayNotification("Export généré à : " + selectedFile.getName(), "green");

        } catch (IOException e) {
            displayNotification("Erreur lors de l'export Excel", "red");
            e.printStackTrace();
        }
    }

    /**
     * Permet à l'utilisateur de choisir le chemin d'export via un FileChooser
     */
    private File chooseExportFile(Stage stage, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter vers Excel");
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        return fileChooser.showSaveDialog(stage);
    }

    // === Notifications temporaires ===
    private void displayNotification(String message, String color) {
        if (notificationLabel != null) {
            notificationLabel.setText(message);
            notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 16px;");
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> notificationLabel.setText(""));
            delay.play();
        } else {
            System.out.println("[NOTIFICATION] " + message);
        }
    }

    /**
     * Méthode liée au bouton "Ajouter" dans CardsFournisseur.fxml
     */
    @FXML
    public void handleAddFournisseur() {
        try {
            // ✅ Chemin absolu depuis resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ajouterFournisseur.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("❌ Erreur : AjoutFournisseurView.fxml introuvable !");
                return;
            }

            Pane root = loader.load();

            AjouterFournisseurController ajoutController = loader.getController();
            ajoutController.setCardsController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau fournisseur");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}