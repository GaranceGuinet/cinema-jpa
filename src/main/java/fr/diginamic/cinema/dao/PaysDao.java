package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Pays;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les pays.
 */
public class PaysDao extends GenericDao<Pays> {

  /**
   * Constructeur du DAO des pays.
   *
   * @param em gestionnaire des entités
   */
  public PaysDao(EntityManager em) {
    super(em, Pays.class);
  }

  /**
   * Recherche un pays à partir de son nom.
   *
   * @param nom nom du pays recherché
   * @return un Optional contenant le pays s'il existe,
   * sinon un Optional vide
   */
  public Optional<Pays> findByNom(String nom) {

    TypedQuery<Pays> queryPays = em.createQuery(
        "SELECT p " +
            "FROM Pays p " +
            "WHERE p.nom = :nom",
        Pays.class);

    queryPays.setParameter("nom", nom);

    List<Pays> pays = queryPays.getResultList();

    return pays.isEmpty()
        ? Optional.empty()
        : Optional.of(pays.get(0));
  }
}