package controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import entities.fournisseur;
import services.FournisseurService;

public class FournisseurController {

    @FXML private TableView<fournisseur> fournisseurTable;
    @FXML private TableColumn<fournisseur, String> nomColumn;
    @FXML private TableColumn<fournisseur, Integer> numColumn;

    @FXML private TextField searchField;
    @FXML private Label notificationLabel;

    private final ObservableList<fournisseur> fournisseurList = FXCollections.observableArrayList();
    private final FournisseurService fournisseurService = new FournisseurService();

    /**
     * Initialize the table columns and load data.
     */
    @FXML
    public void initialize() {
        // Initialize the table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_fourn"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num_fourn"));

        // Load data into the table
        refreshTable();
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
            stage.initModality(Modality.APPLICATION_MODAL); // Make it a modal window
            stage.showAndWait();

            refreshTable(); // Refresh the table after adding
            displayNotification("Supplier added successfully.","green");

        } catch (Exception e) {
            e.printStackTrace();
            displayNotification("Error loading the add supplier interface.", "red");
        }
    }

    /**
     * Open the Edit Supplier window.
     */
    @FXML
    public void openEditWindow() {
        fournisseur selectedSupplier = fournisseurTable.getSelectionModel().getSelectedItem();
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
            displayNotification("Supplier updated successfully.","green");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the selected supplier.
     */
    @FXML
    public void deleteSelectedSupplier() {
        fournisseur selectedSupplier = fournisseurTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showAlert("Error", "No supplier selected!");
            return;
        }

        fournisseurService.supprimerF(selectedSupplier.getId_fourn());
        refreshTable();
        displayNotification("Supplier deleted successfully.","green");
    }

    /**
     * Filter suppliers based on the search text.
     */
    @FXML
    public void filterSuppliers() {
        String filter = searchField.getText().toLowerCase();

        ObservableList<fournisseur> filteredList = FXCollections.observableArrayList();
        for (fournisseur supplier : fournisseurService.afficherF()) {
            if (supplier.getNom_fourn().toLowerCase().contains(filter)) {
                filteredList.add(supplier);
            }
        }

        fournisseurTable.setItems(filteredList);
        displayNotification("Results filtered.","green");
    }

    /**
     * Reset the filter and refresh the full table.
     */
    @FXML
    public void resetFilter() {
        searchField.clear();
        refreshTable();
        displayNotification("Filter reset.","green");
    }

    /**
     * Show an alert dialog.
     *
     * @param title   The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px;");

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> notificationLabel.setText(""));
        delay.play();
    }
}