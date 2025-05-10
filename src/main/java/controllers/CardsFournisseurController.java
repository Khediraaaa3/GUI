package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import services.FournisseurService;

import java.io.IOException;
import java.util.List;

public class CardsFournisseurController {

    @FXML
    private FlowPane fournisseursContainer;
    @FXML
    private TextField searchField;

    private final FournisseurService service = new FournisseurService();
    private List<Fournisseur> allFournisseurs;

    public void initialize() {
        if (fournisseursContainer == null) return;

        allFournisseurs = service.afficherF();
        loadCards(allFournisseurs);
    }

    public void handleSearch() {
        String keyword = searchField.getText().toLowerCase();

        List<Fournisseur> filteredList = allFournisseurs.stream()
                .filter(f -> f.getNom_fourn().toLowerCase().contains(keyword) ||
                        f.getNum_fourn().toLowerCase().contains(keyword))
                .toList();

        loadCards(filteredList);
    }

    public void handleAddFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ajouterFournisseur.fxml"));
            Pane root = loader.load();

            AjouterFournisseurController ajoutController = loader.getController();
            ajoutController.setParentController(this); // ← passer la référence du contrôleur principal

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau fournisseur");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCards(List<Fournisseur> fournisseurs) {
        fournisseursContainer.getChildren().clear();

        for (Fournisseur f : fournisseurs) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/CardViewFournisseur.fxml"));
                Pane card = loader.load();
                CardViewFournisseurController controller = loader.getController();
                controller.setData(f);
                controller.setParentController(this); // ← passer la référence ici aussi
                fournisseursContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadData() {
        allFournisseurs = service.afficherF();
        loadCards(allFournisseurs);
    }
}