package controllers;

import entities.Materiel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CardViewController {

    @FXML
    private ImageView imageView;
    @FXML
    private Label nomLabel, typeLabel, qteTotLabel, qteDispLabel, idLieuLabel, idFournLabel;
    @FXML
    private VBox detailsContainer;

    private Materiel materiel;

    public void setData(Materiel m) {
        this.materiel = m;

        Image image = new Image(getClass().getResourceAsStream("/org/example/demo/mate.png"));
        imageView.setImage(image);

        nomLabel.setText("Nom : " + m.getNom_mat());
        typeLabel.setText("Type : " + m.getType_mat());
        qteTotLabel.setText(String.valueOf(m.getQte_tot()));
        qteDispLabel.setText(String.valueOf(m.getQte_disp()));
        idLieuLabel.setText(String.valueOf(m.getIdLieu()));
        idFournLabel.setText(String.valueOf(m.getId_fourn()));
    }

    @FXML
    private void showDetails() {
        detailsContainer.setVisible(true);
    }

    @FXML
    private void modifier() {
        System.out.println("Modifier : " + materiel.getNom_mat());
    }

    @FXML
    private void supprimer() {
        System.out.println("Supprimer : " + materiel.getNom_mat());
    }
}