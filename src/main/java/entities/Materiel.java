package entities;

public class Materiel {
    private int id_mat;
    private String nom_mat;
    private String type_mat;
    private int qte_tot;
    private int qte_disp;
    private int idLieu;
    private int id_fourn;

    // ✨ Nouveaux champs pour filtrer/trier
    private double prix;     // Prix du matériel
    private String etat;     // Ex: "Neuf", "Bon état", "Usé"

    public Materiel() {
    }

    // Constructeur sans id (pour les nouveaux matériels)
    public Materiel(String nom_mat, String type_mat, int qte_tot, int qte_disp, int idLieu, int id_fourn, double prix, String etat) {
        this.nom_mat = nom_mat;
        this.type_mat = type_mat;
        this.qte_tot = qte_tot;
        this.qte_disp = qte_disp;
        this.idLieu = idLieu;
        this.id_fourn = id_fourn;
        this.prix = prix;
        this.etat = etat;
    }

    // Constructeur complet (avec id)
    public Materiel(int id_mat, String nom_mat, String type_mat, int qte_tot, int qte_disp, int idLieu, int id_fourn, double prix, String etat) {
        this.id_mat = id_mat;
        this.nom_mat = nom_mat;
        this.type_mat = type_mat;
        this.qte_tot = qte_tot;
        this.qte_disp = qte_disp;
        this.idLieu = idLieu;
        this.id_fourn = id_fourn;
        this.prix = prix;
        this.etat = etat;
    }

    // Getters & Setters existants... (restent inchangés)

    public void setId_mat(int id_mat) { this.id_mat = id_mat; }
    public int getId_mat() { return id_mat; }

    public void setNom_mat(String nom_mat) { this.nom_mat = nom_mat; }
    public String getNom_mat() { return nom_mat; }

    public void setType_mat(String type_mat) { this.type_mat = type_mat; }
    public String getType_mat() { return type_mat; }

    public void setQte_tot(int qte_tot) { this.qte_tot = qte_tot; }
    public int getQte_tot() { return qte_tot; }

    public void setQte_disp(int qte_disp) { this.qte_disp = qte_disp; }
    public int getQte_disp() { return qte_disp; }

    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }
    public int getIdLieu() { return idLieu; }

    public void setId_fourn(int id_fourn) { this.id_fourn = id_fourn; }
    public int getId_fourn() { return id_fourn; }

    // ✨ Getter et Setter pour le nouveau champ : prix
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    // ✨ Getter et Setter pour le nouveau champ : état
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    @Override
    public String toString() {
        return "Material { " +
                "id_mat= " + id_mat
                + ", nom_mat= " + nom_mat
                + ", type_mat= " + type_mat
                + ", qte_tot= " + qte_tot
                + ", qte_disp= " + qte_disp
                + ", idLieu= " + idLieu
                + ", id_fourn= " + id_fourn
                + ", prix= " + prix
                + ", etat= " + etat
                + " }";
    }
}