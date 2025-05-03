package services;
import entities.Fournisseur;
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
    public void ajouterF(Fournisseur f) {
        String sql = "INSERT INTO `Fournisseur`(`nom_fourn`, `num_fourn`) VALUES ('" + f.getNom_fourn() + "'," + f.getNum_fourn() + ")";
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean modifierF(Fournisseur f) {
        String sql = "UPDATE `Fournisseur` SET `nom_fourn`=?, `num_fourn`=? WHERE `id_fourn`=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNom_fourn());
            ps.setString(2, f.getNum_fourn());
            ps.setInt(3, f.getId_fourn());
            ps.executeUpdate();
            // Exécuter la requête et vérifier le nombre de lignes affectées
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Retourne true si au moins une ligne est mise à jour

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du Fournisseur : " + e.getMessage());
            return false; // Retourne false en cas d'erreur
        }
    }

    @Override
    public void supprimerF(int id) {
        String sql = "DELETE FROM `Fournisseur` WHERE `id_fourn`=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Fournisseur> afficherF() {
        List<Fournisseur> Fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM `Fournisseur`";
        try {
            Statement statement = con.createStatement();
            ResultSet rs= statement.executeQuery(sql);
            while (rs.next()){
                Fournisseur f = new Fournisseur();
                f.setId_fourn(rs.getInt(1));
                f.setNom_fourn(rs.getString("nom_fourn"));
                f.setNum_fourn(rs.getString("num_fourn"));
                Fournisseurs.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Fournisseurs;
    }
}