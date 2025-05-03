
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

public class FournisseurController {

    @FXML private TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, String> nomColumn;
    @FXML private TableColumn<Fournisseur, Integer> numColumn;

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
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num_fourn"));

        // Charger les données dans la table
        refreshTable();

        // Remplir le graphique avec des données dynamiques
        //populateBarChart();
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
            displayNotification("Supplier added successfully.", "orange");

        } catch (Exception e) {
            e.printStackTrace();
            displayNotification("Error loading the add supplier interface.", "orange");
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
            displayNotification("Supplier updated successfully.", "orange");

        } catch (Exception e) {
            e.printStackTrace();
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

        fournisseurService.supprimerF(selectedSupplier.getId_fourn());
        refreshTable();
        displayNotification("Supplier deleted successfully.", "orange");
    }

    /**
     * Filter suppliers based on the search text.
     */
    @FXML
    public void filterSuppliers() { // Correction : Assurez-vous que ce nom correspond à celui dans le fichier .fxml
        String filter = searchField.getText().toLowerCase();
        String filter2 = searchField.getText().trim();

        ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
        for (Fournisseur supplier : fournisseurService.afficherF()) {
            if (supplier.getNom_fourn().toLowerCase().contains(filter)) {
                filteredList.add(supplier);
            }
            String phoneNumber = String.valueOf(supplier.getNum_fourn());
            if (phoneNumber.contains(filter2)) {
                filteredList.add(supplier);
            }
        }

        fournisseurTable.setItems(filteredList);
        displayNotification("Results filtered.", "orange");
    }

    /**
     * Reset the filter and refresh the full table.
     */
    @FXML
    public void resetFilter() {
        searchField.clear();
        refreshTable();
        displayNotification("Filter reset.", "orange");
    }

    /**
     * Populate the BarChart with dynamic data.
     */
    /*private void populateBarChart() {
        // Créer une série de données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stock Levels");

        // Simuler des données ou récupérer depuis la base de données
        for (fournisseur supplier : fournisseurService.afficherF()) {
            series.getData().add(new XYChart.Data<>(supplier.getNom_fourn(), supplier.getNum_fourn()));
        }

        // Ajouter la série au graphique
        materialStockChart.getData().clear(); // Nettoyer les anciennes données
        materialStockChart.getData().add(series);
    }*/

    /**
     * Show an alert dialog.
     *
     * @param title   The title of the alert.
     * @param message The message of the alert.
     */
    @FXML
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
        notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> notificationLabel.setText(""));
        delay.play();
    }
}
