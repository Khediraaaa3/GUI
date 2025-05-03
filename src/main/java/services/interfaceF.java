package services;
import entities.Fournisseur;
import java.sql.SQLException;
import java.util.List;

public interface interfaceF {
    public void ajouterF(Fournisseur f);
    public boolean modifierF(Fournisseur f);
    public void supprimerF(int id);
    List<Fournisseur> afficherF();
}