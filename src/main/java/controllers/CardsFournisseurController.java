package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import services.FournisseurService;

import java.io.IOException;
import java.util.List;

public class CardsFournisseurController {

    @FXML
    private FlowPane fournisseursContainer;
    @FXML
    private TextField searchField;

    private final FournisseurService fournisseurService = new FournisseurService();
    private List<Fournisseur> allFournisseurs;

    public void initialize() {
        if (fournisseursContainer == null) return;

        allFournisseurs = fournisseurService.afficherF();
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

    private void loadCards(List<Fournisseur> fournisseurs) {
        fournisseursContainer.getChildren().clear();

        for (Fournisseur f : fournisseurs) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/CardViewFournisseur.fxml"));
                Pane card = loader.load();
                CardViewFournisseurController controller = loader.getController();
                controller.setData(f);
                fournisseursContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}