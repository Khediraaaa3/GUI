package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Entité représentant un fournisseur.
 */

public class Fournisseur {

    private int id_fourn;

    // ✅ Utilisation de JavaFX Property pour le binding UI (optionnel si utilisé avec JavaFX)
    private final StringProperty nom_fourn = new SimpleStringProperty();
    private final StringProperty num_fourn = new SimpleStringProperty();

    public Fournisseur() {}

    public Fournisseur(String nom_fourn, String num_fourn) {
        this.nom_fourn.set(nom_fourn);
        this.num_fourn.set(num_fourn);
    }

    public Fournisseur(int id_fourn, String nom_fourn, String num_fourn) {
        this.id_fourn = id_fourn;
        this.nom_fourn.set(nom_fourn);
        this.num_fourn.set(num_fourn);
    }

    // --- Getters et Setters pour les propriétés JavaFX ---

    public int getId_fourn() {
        return id_fourn;
    }

    public void setId_fourn(int id_fourn) {
        this.id_fourn = id_fourn;
    }

    public String getNom_fourn() {
        return nom_fourn.get();
    }

    public void setNom_fourn(String nom_fourn) {
        this.nom_fourn.set(nom_fourn);
    }

    public StringProperty nom_fournProperty() {
        return nom_fourn;
    }

    public String getNum_fourn() {
        return num_fourn.get();
    }

    public void setNum_fourn(String num_fourn) {
        this.num_fourn.set(num_fourn);
    }

    public StringProperty num_fournProperty() {
        return num_fourn;
    }

    // --- Validation du numéro de téléphone ---

    /**
     * Valide le format du numéro de téléphone (ex: +33 6 12 34 56 78 ou 06-12-34-56-78).
     * @param phoneNumber Le numéro à valider.
     * @return true si le format est valide.
     * @throws IllegalArgumentException Si le numéro est invalide.
     */
    public static boolean validatePhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // ✅ Regex pour le format français (ajustable selon vos besoins)
        String regex = "^[-+\\s\\d()]{7,}$";
        if (!phoneNumber.matches(regex)) {
            throw new IllegalArgumentException("Numéro de téléphone invalide : " + phoneNumber);
        }
        return true;
    }

    // --- toString() ---

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id_fourn=" + id_fourn +
                ", nom_fourn='" + nom_fourn.get() + '\'' +
                ", num_fourn='" + num_fourn.get() + '\'' +
                '}';
    }
}