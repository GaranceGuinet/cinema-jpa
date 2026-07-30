package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Genre;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les genres.
 */
public class GenreDao extends GenericDao<Genre> {

  /**
   * Constructeur du DAO des genres.
   *
   * @param em gestionnaire des entités
   */
  public GenreDao(EntityManager em) {
    super(em, Genre.class);
  }

  /**
   * Recherche un genre à partir de son nom.
   *
   * @param nom nom du genre recherché
   * @return un Optional contenant le genre s'il existe,
   * sinon un Optional vide
   */
  public Optional<Genre> findByNom(String nom) {

    TypedQuery<Genre> queryGenre = em.createQuery(
        "SELECT g " +
            "FROM Genre g " +
            "WHERE g.nom = :nom",
        Genre.class);

    queryGenre.setParameter("nom", nom);

    List<Genre> genres = queryGenre.getResultList();

    return genres.isEmpty()
        ? Optional.empty()
        : Optional.of(genres.get(0));
  }

}