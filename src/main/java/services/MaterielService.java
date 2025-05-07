package services;

import entities.Materiel;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements interfaceM {

    private Connection con;

    public MaterielService() {
        con = MyDataBase.getInstance().getConnection();
    }

    /**
     * Valide les données avant insertion ou modification.
     */
    private boolean isValid(Materiel m) {
        return m.getNom_mat() != null && !m.getNom_mat().trim().isEmpty()
                && m.getType_mat() != null && !m.getType_mat().trim().isEmpty()
                && m.getQte_tot() >= 0
                && m.getQte_disp() >= 0 && m.getQte_disp() <= m.getQte_tot();
    }

    @Override
    public void ajouterM(Materiel m) {
        if (!isValid(m)) {
            System.out.println("Erreur : Données invalides lors de l'ajout du matériel.");
            return;
        }

        String sql = "INSERT INTO materiel (nom_mat, type_mat, qte_tot, qte_disp, IdLieu, id_fourn) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNom_mat());
            ps.setString(2, m.getType_mat());
            ps.setInt(3, m.getQte_tot());
            ps.setInt(4, m.getQte_disp());
            ps.setInt(5, m.getIdLieu());
            ps.setInt(6, m.getId_fourn());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du matériel : " + e.getMessage());
        }
    }

    @Override
    public boolean modifierM(Materiel m) {
        if (!isValid(m)) {
            System.out.println("Erreur : Données invalides lors de la modification du matériel.");
            return false;
        }

        String sql = "UPDATE materiel SET nom_mat=?, type_mat=?, qte_tot=?, qte_disp=?, IdLieu=?, id_fourn=? WHERE id_mat=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNom_mat());
            ps.setString(2, m.getType_mat());
            ps.setInt(3, m.getQte_tot());
            ps.setInt(4, m.getQte_disp());
            ps.setInt(5, m.getIdLieu());
            ps.setInt(6, m.getId_fourn());
            ps.setInt(7, m.getId_mat());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du matériel : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimerM(int id) {
        String sql = "DELETE FROM materiel WHERE id_mat = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du matériel : " + e.getMessage());
            return false;
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
            System.out.println("Erreur lors de l'affichage des matériaux : " + e.getMessage());
        }
        return listeMateriel;
    }
}