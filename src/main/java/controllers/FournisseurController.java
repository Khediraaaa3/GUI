package controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import entities.Fournisseur;
import services.FournisseurService;

import java.util.Map;
import java.util.Optional;

public class FournisseurController {

    @FXML private TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, String> nomColumn; // ✅ Correction : String au lieu de Integer
    @FXML private TableColumn<Fournisseur, String> numColumn;  // ✅ Correction : String au lieu de Integer

    @FXML private TextField searchField;
    @FXML private Label notificationLabel;

    // Ajout du BarChart pour afficher les données dynamiques
    @FXML private BarChart<String, Number> materialStockChart;

    private final ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private final FournisseurService fournisseurService = new FournisseurService();

    /**
     * Initialize the table columns and load data.
     */
    @FXML
    public void initialize() {
        // Initialiser les colonnes de la table
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_fourn"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num_fourn")); // ✅ Utilisation de StringProperty

        // Charger les données dans la table
        refreshTable();

        // Remplir le graphique avec des données dynamiques
    }

    /**
     * Refresh the table with data from the database.
     */
    private void refreshTable() {
        fournisseurList.clear();
        fournisseurList.addAll(fournisseurService.afficherF());
        fournisseurTable.setItems(fournisseurList);
        System.out.println("Table refreshed successfully.");
    }

    /**
     * Open the Add Supplier window.
     */
    @FXML
    public void openAddWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ajouterFournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Supplier");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Fenêtre modale
            stage.showAndWait();

            refreshTable(); // Rafraîchir la table après ajout
            displayNotification("Supplier added successfully.", "green");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load add supplier interface: " + e.getMessage());
        }
    }

    /**
     * Open the Edit Supplier window.
     */
    @FXML
    public void openEditWindow() {
        Fournisseur selectedSupplier = fournisseurTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showAlert("Error", "No supplier selected!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifFournisseur.fxml"));
            Parent root = loader.load();

            ModifFournisseurController controller = loader.getController();
            controller.initData(selectedSupplier);

            Stage stage = new Stage();
            stage.setTitle("Edit Supplier");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();
            displayNotification("Supplier updated successfully.", "green");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load edit supplier interface: " + e.getMessage());
        }
    }

    /**
     * Delete the selected supplier.
     */
    @FXML
    public void deleteSelectedSupplier() {
        Fournisseur selectedSupplier = fournisseurTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showAlert("Error", "No supplier selected!");
            return;
        }

        // ✅ Confirmation avant suppression
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supplier?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = fournisseurService.supprimerF(selectedSupplier.getId_fourn());
            if (deleted) {
                refreshTable();
                displayNotification("Supplier deleted successfully.", "green");
            } else {
                showAlert("Error", "Failed to delete supplier.");
            }
        }
    }

    /**
     * Filter suppliers based on the search text.
     */
    @FXML
    public void filterSuppliers() {
        String filter = searchField.getText().toLowerCase();
        String filter2 = searchField.getText().trim();

        ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
        for (Fournisseur supplier : fournisseurService.afficherF()) {
            boolean matchNom = supplier.getNom_fourn().toLowerCase().contains(filter);
            boolean matchNum = String.valueOf(supplier.getNum_fourn()).contains(filter2);
            if (matchNom || matchNum) { // ✅ Correction : éviter les doublons
                filteredList.add(supplier);
            }
        }

        fournisseurTable.setItems(filteredList);
        displayNotification("Results filtered.", "green");
    }

    /**
     * Reset the filter and refresh the full table.
     */
    @FXML
    public void resetFilter() {
        searchField.clear();
        refreshTable();
        displayNotification("Filter reset.", "green");
    }


    /**
     * Show an alert dialog.
     *
     * @param title   The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // ✅ Utilisation d'AlertType.ERROR pour les erreurs
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display a temporary notification.
     *
     * @param message The message to display.
     * @param color   The text color ("green" or "red").
     */
    private void displayNotification(String message, String color) {
        notificationLabel.setText(message);
        notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> notificationLabel.setText(""));
        delay.play();
    }
}