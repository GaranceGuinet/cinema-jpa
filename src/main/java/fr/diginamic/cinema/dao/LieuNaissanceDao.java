package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.LieuNaissance;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les lieux de naissance.
 */
public class LieuNaissanceDao extends GenericDao<LieuNaissance> {

  /**
   * Constructeur du DAO des lieux de naissance.
   *
   * @param em gestionnaire des entités
   */
  public LieuNaissanceDao(EntityManager em) {
    super(em, LieuNaissance.class);
  }

  /**
   * Recherche un lieu de naissance à partir de son nom.
   *
   * @param nom nom du lieu de naissance recherché
   * @return un Optional contenant le lieu de naissance s'il existe,
   * sinon un Optional vide
   */
  public Optional<LieuNaissance> findByNom(String nom) {

    TypedQuery<LieuNaissance> queryLieuNaissance = em.createQuery(
        "SELECT l " +
            "FROM LieuNaissance l " +
            "WHERE l.nom = :nom",
        LieuNaissance.class);

    queryLieuNaissance.setParameter("nom", nom);

    List<LieuNaissance> lieuxNaissance =
        queryLieuNaissance.getResultList();

    return lieuxNaissance.isEmpty()
        ? Optional.empty()
        : Optional.of(lieuxNaissance.get(0));
  }
}