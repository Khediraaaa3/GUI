    package controllers;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Label;
    import javafx.stage.Modality;
    import javafx.stage.Stage;

    import javafx.fxml.FXML;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;

    import java.io.IOException;

    public class MainAppController {

        @FXML private ImageView logoImage;

        @FXML private Label notificationLabel;


        @FXML
        public void initialize() {
            // Charger l'image du logo
            try {
                Image logo = new Image(getClass().getResource("/org/example/demo/logo.png").toExternalForm());
                logoImage.setImage(logo);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image du logo : " + e.getMessage());
            }
        }
    /**
     * Open the Materials Management interface.
     */
    @FXML
    public void openSuppliersInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/fournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Suppliers Management");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            displayNotification("Suppliers interface closed.", "green");

        } catch (IOException e) {
            e.printStackTrace();
            displayNotification("Error loading the suppliers interface.", "red");
        }
    }

        @FXML
        public void openMaterialsInterface() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Materiels.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Materials Management");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                displayNotification("Materials interface closed.", "green");

            } catch (IOException e) {
                e.printStackTrace();
                displayNotification("Error loading the materials interface.", "red");
            }
        }

        @FXML
        public void exitApplication() {
            System.exit(0);
        }

        private void displayNotification(String message, String color) {
            notificationLabel.setText(message);
            notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px;");
        }

}