module org.example.demo {
    // Déclarer les modules requis
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires javafx.controls;
    requires mysql.connector.j;
    requires itextpdf;
    requires org.apache.poi.ooxml;

    // Ouvrir les packages nécessaires à JavaFX
    opens test to javafx.fxml;         // Pour le package contenant MainApplication
    opens controllers to javafx.fxml;  // Pour les contrôleurs
    opens entities to javafx.fxml;     // Pour les modèles (Materiel)
    opens services to javafx.fxml;     // Pour les services (MaterielService)

    // Exporter les packages principaux
    exports test;         // Exporter le package test
    exports controllers;  // Exporter les contrôleurs
    exports entities;     // Exporter les modèles
    exports services;     // Exporter les services
}