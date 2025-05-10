package controllers;

import entities.Materiel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.MaterielService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class CardsController {

    @FXML private FlowPane cardsContainerInner;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilter;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<String> etatFilter;
    @FXML private ComboBox<String> sortField;
    @FXML private Button resetFilterButton;
    @FXML private Button addButton;

    private final MaterielService materielService = new MaterielService();
    private List<Materiel> allMateriaux;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void initialize() {
        if (cardsContainerInner == null) {
            System.err.println("🚨 cardsContainerInner est NULL !");
            return;
        }

        allMateriaux = materielService.afficherM();
        loadCards(allMateriaux);

        fillFilters();
        setupDynamicSearch();
        setupFieldListeners();

        resetFilterButton.setOnAction(event -> resetFilters());
    }

    public void reloadData() {
        allMateriaux = materielService.afficherM(); // Recharge depuis la source
        loadCards(allMateriaux); // Rafraîchit l'affichage
    }

    private void fillFilters() {
        typeFilter.setItems(javafx.collections.FXCollections.observableArrayList(
                allMateriaux.stream().map(Materiel::getType_mat).distinct().toList()
        ));
        etatFilter.setItems(javafx.collections.FXCollections.observableArrayList("Neuf", "Bon état", "Usé"));
    }

    private void setupDynamicSearch() {
        searchField.textProperty().addListener((obs, oldText, newText) -> filterAndReload());
        typeFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterAndReload());
        etatFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterAndReload());
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> filterAndReload());
        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> filterAndReload());
        sortField.valueProperty().addListener((obs, oldVal, newVal) -> filterAndReload());
    }

    private void setupFieldListeners() {
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("\\d*([\\.]\\d*)?")) {
                minPriceField.setText(oldVal);
            }
        });

        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("\\d*([\\.]\\d*)?")) {
                maxPriceField.setText(oldVal);
            }
        });
    }

    private void filterAndReload() {
        scheduler.schedule(() -> {
            List<Materiel> filteredList = allMateriaux.stream()
                    .filter(createCombinedPredicate())
                    .sorted((a, b) -> {
                        String sort = sortField.getValue();
                        if ("Prix croissant".equals(sort)) {
                            return Double.compare(a.getPrix(), b.getPrix());
                        } else if ("Prix décroissant".equals(sort)) {
                            return Double.compare(b.getPrix(), a.getPrix());
                        }
                        return 0;
                    })
                    .toList();

            Platform.runLater(() -> loadCards(filteredList));
        }, 300, TimeUnit.MILLISECONDS);
    }

    private Predicate<Materiel> createCombinedPredicate() {
        String keyword = searchField.getText().toLowerCase();
        String selectedType = typeFilter.getValue();
        String selectedEtat = etatFilter.getValue();
        Double minPrice = parsePrice(minPriceField.getText());
        Double maxPrice = parsePrice(maxPriceField.getText());

        return m -> {
            boolean matchKeyword = keyword.isEmpty() ||
                    m.getNom_mat().toLowerCase().contains(keyword) ||
                    m.getType_mat().toLowerCase().contains(keyword);

            boolean matchType = selectedType == null || selectedType.isEmpty() ||
                    m.getType_mat().equals(selectedType);

            boolean matchEtat = selectedEtat == null || selectedEtat.isEmpty() ||
                    m.getEtat().equals(selectedEtat);

            boolean matchMinPrice = minPrice == null || m.getPrix() >= minPrice;
            boolean matchMaxPrice = maxPrice == null || m.getPrix() <= maxPrice;

            return matchKeyword && matchType && matchEtat && matchMinPrice && matchMaxPrice;
        };
    }

    private Double parsePrice(String text) {
        try {
            return text == null || text.isBlank() ? null : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void resetFilters() {
        searchField.setText("");
        typeFilter.getSelectionModel().clearSelection();
        minPriceField.setText("");
        maxPriceField.setText("");
        etatFilter.getSelectionModel().clearSelection();
        sortField.getSelectionModel().clearSelection();

        loadCards(allMateriaux);
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

    public void handleAddMaterial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ajouterMateriel.fxml"));
            Pane root = loader.load();

            AjouterMaterielController ajoutController = loader.getController();
            ajoutController.setCardsController(this); // Passe la référence du contrôleur courant

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau matériel");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur", "Impossible de charger l'interface d'ajout.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}