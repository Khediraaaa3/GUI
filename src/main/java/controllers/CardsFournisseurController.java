package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.FournisseurService;

import java.io.IOException;
import java.util.List;

public class CardsFournisseurController implements Cards {

    @FXML private FlowPane fournisseursContainer;

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