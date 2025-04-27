package services;
import entities.fournisseur;
import java.sql.SQLException;
import java.util.List;

public interface interfaceF {
    public void ajouterF(fournisseur f);
    public boolean modifierF(fournisseur f);
    public void supprimerF(int id);
    List<fournisseur> afficherF();
}
