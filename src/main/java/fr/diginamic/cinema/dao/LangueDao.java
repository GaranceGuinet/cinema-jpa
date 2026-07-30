package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Langue;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les langues.
 */
public class LangueDao extends GenericDao<Langue> {

  /**
   * Constructeur du DAO des langues.
   *
   * @param em gestionnaire des entités
   */
  public LangueDao(EntityManager em) {
    super(em, Langue.class);
  }

  /**
   * Recherche une langue à partir de son nom.
   *
   * @param nom nom de la langue recherchée
   * @return un Optional contenant la langue si elle existe,
   * sinon un Optional vide
   */
  public Optional<Langue> findByNom(String nom) {

    TypedQuery<Langue> queryLangue = em.createQuery(
        "SELECT l " +
            "FROM Langue l " +
            "WHERE l.nom = :nom",
        Langue.class);

    queryLangue.setParameter("nom", nom);

    List<Langue> langues = queryLangue.getResultList();

    return langues.isEmpty()
        ? Optional.empty()
        : Optional.of(langues.get(0));
  }
}