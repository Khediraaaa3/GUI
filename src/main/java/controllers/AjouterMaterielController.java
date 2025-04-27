package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.MaterielService;
import entities.Materiel;
import java.time.LocalDate;

public class AjouterMaterielController {
    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField quantiteField;

    private MaterielService materielService = new MaterielService();

    @FXML
    public void ajouterMateriel(ActionEvent event) {
        String nom = nomField.getText();
        String description = descriptionField.getText();
        int quantite = Integer.parseInt(quantiteField.getText());

        Materiel materiel = new Materiel(nom, description, quantite, LocalDate.now());
        materielService.ajouterMateriel(materiel);

        // Réinitialiser les champs après ajout
        nomField.clear();
        descriptionField.clear();
        quantiteField.clear();
    }
}