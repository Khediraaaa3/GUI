// NavigationController.java
package controllers; // Remplace par ton package réel

import controllers.MainAppController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class NavigationController {

    // Méthodes de navigation
    @FXML
    private VBox floatingMenu;

    @FXML
    private void toggleFloatingMenu() {
        boolean isVisible = floatingMenu.isVisible();
        floatingMenu.setVisible(!isVisible);

        TranslateTransition tt = new TranslateTransition(Duration.millis(300), floatingMenu);
        tt.setFromX(isVisible ? 0 : -250);
        tt.setToX(isVisible ? -250 : 0);
        tt.play();
    }

    @FXML
    private void openMainApp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/mainApp.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-image: url('" + getClass().getResource("/org/example/demo/background3.jpg") + "');" +
                    "-fx-background-size: cover;" +
                    "-fx-background-position: center center;" +
                    "-fx-background-repeat: no-repeat;");

            Stage stage = new Stage();
            stage.setTitle("Suppliers Management");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();



        } catch (IOException e) {
            e.printStackTrace();

        }


    }

}