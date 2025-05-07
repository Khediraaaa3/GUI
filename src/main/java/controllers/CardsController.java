package controllers;

import entities.Materiel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import services.MaterielService;

import java.io.IOException;
import java.util.List;

public class CardsController {

    @FXML
    private FlowPane cardsContainerInner; // ✅ Correspond à fx:id="cardsContainerInner"

    @FXML
    private TextField searchField;

    private final MaterielService materielService = new MaterielService();
    private List<Materiel> allMateriaux;

    public void initialize() {
        if (cardsContainerInner == null) {
            System.err.println("🚨 cardsContainerInner est NULL !");
            return;
        }

        allMateriaux = materielService.afficherM();
        loadCards(allMateriaux);
    }

    public void handleSearch() {
        String keyword = searchField.getText().toLowerCase();

        List<Materiel> filteredList = allMateriaux.stream()
                .filter(m -> m.getNom_mat().toLowerCase().contains(keyword) ||
                        m.getType_mat().toLowerCase().contains(keyword))
                .toList();

        loadCards(filteredList);
    }

    private void loadCards(List<Materiel> materiaux) {
        cardsContainerInner.getChildren().clear();

        for (Materiel m : materiaux) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/CardView.fxml"));
                Pane card = loader.load();
                CardViewController controller = loader.getController();
                controller.setData(m);
                cardsContainerInner.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}