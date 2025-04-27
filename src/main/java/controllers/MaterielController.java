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
import entities.Materiel;
import services.MaterielService;

public class MaterielController {

    @FXML private TableView<Materiel> materielTable;
    @FXML private TableColumn<Materiel, String> nomColumn;
    @FXML private TableColumn<Materiel, String> typeColumn;
    @FXML private TableColumn<Materiel, Integer> qteTotColumn;
    @FXML private TableColumn<Materiel, Integer> qteDispColumn;
    @FXML private TableColumn<Materiel, Integer> idLieuColumn;
    @FXML private TableColumn<Materiel, Integer> idFournColumn;

    @FXML private TextField searchField;
    @FXML private Label notificationLabel;

    private final ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private final MaterielService materielService = new MaterielService();

    /**
     * Initialize the table columns and load data.
     */
    @FXML
    public void initialize() {
        // Initialize the table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_mat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_mat"));
        qteTotColumn.setCellValueFactory(new PropertyValueFactory<>("qte_tot"));
        qteDispColumn.setCellValueFactory(new PropertyValueFactory<>("qte_disp"));
        idLieuColumn.setCellValueFactory(new PropertyValueFactory<>("IdLieu"));
        idFournColumn.setCellValueFactory(new PropertyValueFactory<>("id_fourn"));

        // Load data into the table
        refreshTable();
    }

    /**
     * Refresh the table with data from the database.
     */
    private void refreshTable() {
        materielList.clear();
        materielList.addAll(materielService.afficherM());
        materielTable.setItems(materielList);
        System.out.println("Table refreshed successfully.");
    }

    /**
     * Open the Add Material window.
     */
    @FXML
    public void openAddWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ajouterMateriel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Material");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();
            displayNotification("Material added successfully.","green");

        } catch (Exception e) {
            e.printStackTrace();
            displayNotification("Error loading the add material interface.", "red");
        }
    }

    /**
     * Open the Edit Material window.
     */
    @FXML
    public void openEditWindow() {
        Materiel selectedMaterial = materielTable.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            showAlert("Error", "No material selected!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifMateriel.fxml"));
            Parent root = loader.load();

            ModifMaterielController controller = loader.getController();
            controller.initData(selectedMaterial);

            Stage stage = new Stage();
            stage.setTitle("Edit Material");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();
            displayNotification("Material updated successfully.","green");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the selected material.
     */
    @FXML
    public void deleteSelectedMaterial() {
        Materiel selectedMaterial = materielTable.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            showAlert("Error", "No material selected!");
            return;
        }

        materielService.supprimerM(selectedMaterial.getId_mat());
        refreshTable();
        displayNotification("Material deleted successfully.","green");
    }

    /**
     * Filter materials based on the search text.
     */
    @FXML
    public void filterMaterials() {
        String filter = searchField.getText().toLowerCase();

        ObservableList<Materiel> filteredList = FXCollections.observableArrayList();
        for (Materiel material : materielService.afficherM()) {
            if (material.getNom_mat().toLowerCase().contains(filter) || material.getType_mat().toLowerCase().contains(filter)) {
                filteredList.add(material);
            }
        }

        materielTable.setItems(filteredList);
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
    }
}