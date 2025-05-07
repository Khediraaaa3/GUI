package services;

import entities.Fournisseur;
import utils.MyDataBase;

import java.sql.*;

public class FournisseurService implements interfaceF {

    private Connection con;

    public FournisseurService() {
        con = MyDataBase.getInstance().getConnection();
    }

    /**
     * Valide les données avant insertion ou modification.
     */
    private boolean isValid(Fournisseur f) {
        return f.getNom_fourn() != null && !f.getNom_fourn().trim().isEmpty()
                && Fournisseur.validatePhoneNumber(f.getNum_fourn());
    }

    @Override
    public void ajouterF(Fournisseur f) {
        if (!isValid(f)) {
            System.out.println("Erreur : Données invalides lors de l'ajout du fournisseur.");
            return;
        }

        String sql = "INSERT INTO fournisseur (nom_fourn, num_fourn) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getNom_fourn());
            ps.setString(2, f.getNum_fourn());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du fournisseur : " + e.getMessage());
        }
    }

    @Override
    public boolean modifierF(Fournisseur f) {
        if (!isValid(f)) {
            System.out.println("Erreur : Données invalides lors de la modification du fournisseur.");
            return false;
        }

        String sql = "UPDATE fournisseur SET nom_fourn=?, num_fourn=? WHERE id_fourn=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getNom_fourn());
            ps.setString(2, f.getNum_fourn());
            ps.setInt(3, f.getId_fourn());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du fournisseur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimerF(int id) {
        String sql = "DELETE FROM fournisseur WHERE id_fourn = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du fournisseur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public java.util.List<Fournisseur> afficherF() {
        java.util.List<Fournisseur> fournisseurs = new java.util.ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Fournisseur f = new Fournisseur(
                        rs.getInt("id_fourn"),
                        rs.getString("nom_fourn"),
                        rs.getString("num_fourn")
                );
                fournisseurs.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des fournisseurs : " + e.getMessage());
        }
        return fournisseurs;
    }
}