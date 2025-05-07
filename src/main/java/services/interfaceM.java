package services;

import entities.Materiel;

import java.util.List;

public interface interfaceM {
    void ajouterM(Materiel m);
    boolean modifierM(Materiel m);
    boolean supprimerM(int id);
    List<Materiel> afficherM ();
}
