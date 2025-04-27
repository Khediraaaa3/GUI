package entities;

public class fournisseur {
    private int id_fourn;
    private String nom_fourn;
    private int num_fourn;

    public fournisseur() {
    }

    public fournisseur(String nom_fourn, int num_fourn) {
        this.nom_fourn = nom_fourn;
        this.num_fourn = num_fourn;
    }

    public fournisseur(int id_fourn, String nom_fourn, int num_fourn) {
        this.id_fourn = id_fourn;
        this.nom_fourn = nom_fourn;
        this.num_fourn = num_fourn;
    }

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
    public int getNum_fourn() {
        return num_fourn;
    }
    public void setNum_fourn(int num_fourn) {
        this.num_fourn = num_fourn;
    }

    @Override
    public String toString() {
        return "fournisseur { " +
                "id_fourn= " + id_fourn +
                ", nom_fourn= '" + nom_fourn +
                ", num_fourn= " + num_fourn +
                " }";
    }
}
