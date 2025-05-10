package controllers;

import entities.Materiel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.MaterielService;

public class ModifMaterielController {

    @FXML private TextField nomField, typeField, qteTotField, qteDispField;
    @FXML private ComboBox<String> etatComboBox;
    @FXML private Label messageLabel;

    private final MaterielService service = new MaterielService();
    private Materiel materielSelectionne;
    private Cards parentController;

    public void initData(Materiel m) {
        this.materielSelectionne = m;

        nomField.setText(m.getNom_mat());
        typeField.setText(m.getType_mat());
        qteTotField.setText(String.valueOf(m.getQte_tot()));
        qteDispField.setText(String.valueOf(m.getQte_disp()));
        etatComboBox.getItems().addAll("Neuf", "Bon état", "Usé");
        etatComboBox.setValue(m.getEtat());
    }

    public void setParentController(Cards controller) {
        this.parentController = controller;
    }

    @FXML
    public void saveMaterial(ActionEvent event) {
        String name = nomField.getText().trim();
        String type = typeField.getText().trim();
        int qteTot = Integer.parseInt(qteTotField.getText().trim());
        int qteDisp = Integer.parseInt(qteDispField.getText().trim());
        String etat = etatComboBox.getValue();

        materielSelectionne.setNom_mat(name);
        materielSelectionne.setType_mat(type);
        materielSelectionne.setQte_tot(qteTot);
        materielSelectionne.setQte_disp(qteDisp);
        materielSelectionne.setEtat(etat);

        boolean success = service.modifierM(materielSelectionne);
        if (success && parentController != null) {
            parentController.reloadData(); // ✅ Rafraîchissement générique
        }

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void goBackToCards(ActionEvent event) {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}