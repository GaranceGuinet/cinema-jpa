package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Realisateur;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les réalisateurs.
 */
public class RealisateurDao extends GenericDao<Realisateur> {

  /**
   * Constructeur du DAO des réalisateurs.
   *
   * @param em gestionnaire des entités
   */
  public RealisateurDao(EntityManager em) {
    super(em, Realisateur.class);
  }

  /**
   * Recherche un réalisateur à partir de son identifiant IMDB.
   *
   * @param idImdb identifiant IMDB du réalisateur recherché
   * @return un Optional contenant le réalisateur s'il existe,
   * sinon un Optional vide
   */
  public Optional<Realisateur> findByIdImdb(String idImdb) {

    TypedQuery<Realisateur> queryRealisateur = em.createQuery(
        "SELECT r " +
            "FROM Realisateur r " +
            "WHERE r.idImdb = :idImdb",
        Realisateur.class);

    queryRealisateur.setParameter("idImdb", idImdb);

    List<Realisateur> realisateurs = queryRealisateur.getResultList();

    return realisateurs.isEmpty()
        ? Optional.empty()
        : Optional.of(realisateurs.get(0));
  }
}