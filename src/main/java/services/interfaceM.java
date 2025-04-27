package services;

import entities.Materiel;
import java.sql.SQLException;
import java.util.List;

public interface interfaceM {
    void ajouterM(Materiel m);
    boolean modifierM(Materiel m);
    void supprimerM(int id);
    List<Materiel> afficherM ();
}
