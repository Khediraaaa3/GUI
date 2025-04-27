package services;
import entities.fournisseur;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurService implements interfaceF{

    private Connection con;
    public FournisseurService() {
        con = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouterF(fournisseur f) {
        String sql = "INSERT INTO `fournisseur`(`nom_fourn`, `num_fourn`) VALUES ('" + f.getNom_fourn() + "'," + f.getNum_fourn() + ")";
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean modifierF(fournisseur f) {
        String sql = "UPDATE `fournisseur` SET `nom_fourn`=?, `num_fourn`=? WHERE `id_fourn`=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNom_fourn());
            ps.setInt(2, f.getNum_fourn());
            ps.setInt(3, f.getId_fourn());
            ps.executeUpdate();
            // Exécuter la requête et vérifier le nombre de lignes affectées
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Retourne true si au moins une ligne est mise à jour

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du fournisseur : " + e.getMessage());
            return false; // Retourne false en cas d'erreur
        }
    }

    @Override
    public void supprimerF(int id) {
        String sql = "DELETE FROM `fournisseur` WHERE `id_fourn`=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<fournisseur> afficherF() {
        List<fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM `fournisseur`";
        try {
            Statement statement = con.createStatement();
            ResultSet rs= statement.executeQuery(sql);
            while (rs.next()){
                fournisseur f = new fournisseur();
                f.setId_fourn(rs.getInt(1));
                f.setNom_fourn(rs.getString("nom_fourn"));
                f.setNum_fourn(rs.getInt("num_fourn"));
                fournisseurs.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return fournisseurs;
    }
}
