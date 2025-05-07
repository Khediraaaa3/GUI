package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import entities.Materiel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.MaterielService;

import java.util.List;

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
    @FXML private Button menuButton;
    @FXML private MenuButton dropdownMenu;

    private final MaterielService materielService = new MaterielService();

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_mat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_mat"));
        qteTotColumn.setCellValueFactory(new PropertyValueFactory<>("qte_tot"));
        qteDispColumn.setCellValueFactory(new PropertyValueFactory<>("qte_disp"));
        idLieuColumn.setCellValueFactory(new PropertyValueFactory<>("IdLieu"));
        idFournColumn.setCellValueFactory(new PropertyValueFactory<>("id_fourn"));

        refreshTable();
    }

    private void refreshTable() {
        materielTable.getItems().clear();
        materielTable.setItems(FXCollections.observableArrayList(materielService.afficherM()));
    }

    /**
     * Ouvre le formulaire d'ajout
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
            displayNotification("Material added successfully.", "green");

        } catch (Exception e) {
            displayNotification("Error loading add form.", "red");
        }
    }

    /**
     * Ouvre le formulaire de modification
     */
    @FXML
    public void openEditWindow() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            displayNotification("No material selected!", "red");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/modifMateriel.fxml"));
            Parent root = loader.load();

            ModifMaterielController controller = loader.getController();
            controller.initData(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Material");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();
            displayNotification("Material updated.", "green");

        } catch (Exception e) {
            displayNotification("Error loading edit form.", "red");
        }
    }

    /**
     * Supprime le matériau sélectionné
     */
    @FXML
    public void deleteSelectedMaterial() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            displayNotification("No material selected!", "red");
            return;
        }

        boolean deleted = materielService.supprimerM(selected.getId_mat());
        if (deleted) {
            refreshTable();
            displayNotification("Material deleted.", "green");
        } else {
            displayNotification("Failed to delete material.", "red");
        }
    }

    /**
     * Affiche les résultats filtrés
     */
    @FXML
    public void filterMaterials() {
        String filter = searchField.getText().toLowerCase();
        List<Materiel> filteredList = materielService.afficherM().stream()
                .filter(m -> m.getNom_mat().toLowerCase().contains(filter) ||
                        m.getType_mat().toLowerCase().contains(filter))
                .toList();

        materielTable.setItems(FXCollections.observableArrayList(filteredList));
        displayNotification("Results filtered.", "orange");
    }

    /**
     * Réinitialise le filtre
     */
    @FXML
    public void resetFilter() {
        searchField.clear();
        refreshTable();
        displayNotification("Filter reset.", "orange");
    }

    /**
     * Active/désactive le menu déroulant
     */
    @FXML
    public void toggleDropdownMenu() {
        dropdownMenu.setVisible(!dropdownMenu.isVisible());
        dropdownMenu.setManaged(dropdownMenu.isVisible());
    }

    /**
     * Affiche un message temporaire.
     */
    private void displayNotification(String message, String color) {
        notificationLabel.setText(message);
        notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> notificationLabel.setText(""));
        delay.play();
    }
}