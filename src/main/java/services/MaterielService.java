package services;
import entities.Materiel;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements interfaceM{

    private Connection con;
    public MaterielService() {
        con = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouterM(Materiel m) {
        String sql = "INSERT INTO `materiel`(`nom_mat`, `type_mat`, `qte_tot`, `qte_disp`, `IdLieu`, `id_fourn`) VALUES ('"+m.getNom_mat()+"','"+m.getType_mat()+"',"+m.getQte_tot()+","+m.getQte_disp()+","+m.getIdLieu()+","+m.getId_fourn()+")";
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean modifierM(Materiel m) {
        String sql = "UPDATE `materiel` SET `nom_mat`=?, `type_mat`=?, `qte_tot`=?, `qte_disp`=?, `IdLieu`=?, `id_fourn`=? WHERE `id_mat`=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNom_mat());
            ps.setString(2, m.getType_mat());
            ps.setInt(3, m.getQte_tot());
            ps.setInt(4, m.getQte_disp());
            ps.setInt(5, m.getIdLieu());
            ps.setInt(6, m.getId_fourn());
            ps.setInt(7, m.getId_mat());

            // Exécuter la requête et vérifier le nombre de lignes affectées
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Retourne true si au moins une ligne est mise à jour

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du matériel : " + e.getMessage());
            return false; // Retourne false en cas d'erreur
        }
    }


    @Override
    public void supprimerM(int id) {
        String sql = "DELETE FROM `materiel` WHERE `id_mat`=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Materiel> afficherM() {
        List<Materiel> listeMateriel = new ArrayList<>();
        String sql = "SELECT * FROM materiel";
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Materiel m = new Materiel(
                        rs.getInt("id_mat"),
                        rs.getString("nom_mat"),
                        rs.getString("type_mat"),
                        rs.getInt("qte_tot"),
                        rs.getInt("qte_disp"),
                        rs.getInt("IdLieu"),
                        rs.getInt("id_fourn")
                );
                listeMateriel.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeMateriel;
    }
}
