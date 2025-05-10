package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.FournisseurService;

public class CardViewFournisseurController {

    @FXML private ImageView imageView;
    @FXML private Label nomLabel, numLabel, idLabel;
    @FXML private VBox detailsContainer;

    private Fournisseur fournisseur;
    private CardsFournisseurController parentController;

    public void setData(Fournisseur f) {
        this.fournisseur = f;

        // ✅ Chargement sécurisé de l'image
        try {
            Image image = new Image(getClass().getResourceAsStream("/org/example/demo/supp.png"));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Erreur : Image supp.png introuvable !");
            e.printStackTrace();
        }

        nomLabel.setText("Nom : " + f.getNom_fourn());
        numLabel.setText("Tél : " + f.getNum_fourn());
        idLabel.setText("ID : " + f.getId_fourn());
    }

    public void setParentController(CardsFournisseurController controller) {
        this.parentController = controller;
    }

    @FXML
    private void showDetails() {
        detailsContainer.setVisible(!detailsContainer.isVisible());
    }

    @FXML
    private void modifier() {
        System.out.println("Modifier : " + fournisseur.getNom_fourn());
    }

    @FXML
    private void supprimer() {
        new FournisseurService().supprimerF(fournisseur.getId_fourn());

        if (parentController != null) {
            parentController.reloadData(); // Rafraîchir la liste
        }
    }
}