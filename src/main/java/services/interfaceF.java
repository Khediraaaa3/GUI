package services;
import entities.Fournisseur;

import java.util.List;

public interface interfaceF {
    public void ajouterF(Fournisseur f);
    public boolean modifierF(Fournisseur f);
    public boolean supprimerF(int id);
    List<Fournisseur> afficherF();
}