package entities;

public class Fournisseur {
    private int id_fourn;
    private String nom_fourn;
    private String num_fourn; // ✅ Changé en String pour les numéros avec symboles

    public Fournisseur() {}

    public Fournisseur(String nom_fourn, String num_fourn) {
        this.nom_fourn = nom_fourn;
        this.num_fourn = num_fourn;
    }

    public Fournisseur(int id_fourn, String nom_fourn, String num_fourn) {
        this.id_fourn = id_fourn;
        this.nom_fourn = nom_fourn;
        this.num_fourn = num_fourn;
    }

    // Getters et Setters

    public int getId_fourn() {
        return id_fourn;
    }

    public void setId_fourn(int id_fourn) {
        this.id_fourn = id_fourn;
    }

    public String getNom_fourn() {
        return nom_fourn;
    }

    public void setNom_fourn(String nom_fourn) {
        this.nom_fourn = nom_fourn;
    }

    public String getNum_fourn() {
        return num_fourn;
    }

    public void setNum_fourn(String num_fourn) {
        this.num_fourn = num_fourn;
    }

    // Méthode toString()

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id_fourn=" + id_fourn +
                ", nom_fourn='" + nom_fourn + '\'' +
                ", num_fourn='" + num_fourn + '\'' +
                '}';
    }

    // Méthode de validation (optionnelle mais utile)

    /**
     * Valide le format du numéro de téléphone.
     * @param phoneNumber Le numéro à valider.
     * @return true si le format est valide (ex: +33 6 12 34 56 78)
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        // Exemple de regex : accepte les numéros avec ou sans préfixe + et espaces
        return phoneNumber.matches("\\+?[0-9\\s\\-]{7,}");
    }
}