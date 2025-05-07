package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CardViewFournisseurController {

    @FXML private ImageView imageView;
    @FXML private Label nomLabel, numLabel, idLabel;
    @FXML private VBox detailsContainer;

    private Fournisseur fournisseur;

    public void setData(Fournisseur f) {
        this.fournisseur = f;

        // Charger l’image statique
        Image image = new Image(getClass().getResourceAsStream("/org/example/demo/supp.png"));
        imageView.setImage(image);

        // Données du fournisseur
        nomLabel.setText("Nom : " + fournisseur.getNom_fourn());
        numLabel.setText("Téléphone : " + fournisseur.getNum_fourn());
        idLabel.setText(String.valueOf(fournisseur.getId_fourn()));
    }

    @FXML
    private void showDetails() {
        detailsContainer.setVisible(true);
    }

    @FXML
    private void modifier() {
        System.out.println("Modifier : " + fournisseur.getNom_fourn());
    }

    @FXML
    private void supprimer() {
        System.out.println("Supprimer : " + fournisseur.getNom_fourn());
    }
}