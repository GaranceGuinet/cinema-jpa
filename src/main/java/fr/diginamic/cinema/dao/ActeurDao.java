package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Acteur;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

import fr.diginamic.cinema.entite.Film;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les acteurs.
 */
public class ActeurDao extends GenericDao<Acteur> {

  /**
   * Constructeur du DAO des acteurs.
   *
   * @param em gestionnaire des entités
   */
  public ActeurDao(EntityManager em) {
    super(em, Acteur.class);
  }

  /**
   * Recherche un acteur à partir de son identifiant IMDB.
   *
   * @param idImdb identifiant IMDb de l'acteur recherché
   * @return un Optional contenant l'acteur s'il existe,
   * sinon un Optional vide
   */
  public Optional<Acteur> findByIdImdb(String idImdb) {

    TypedQuery<Acteur> queryActeur = em.createQuery(
        "SELECT a " +
            "FROM Acteur a " +
            "WHERE a.idImdb = :idImdb",
        Acteur.class);

    queryActeur.setParameter("idImdb", idImdb);

    List<Acteur> acteurs = queryActeur.getResultList();

    return acteurs.isEmpty()
        ? Optional.empty()
        : Optional.of(acteurs.get(0));
  }

  /**
   * Recherche les acteurs appartenant au casting principal d'un film.
   *
   * @param film film dont le casting principal est recherché
   * @return la liste des acteurs appartenant au casting principal du film
   */
  public List<Acteur> findByFilm(Film film) {

    TypedQuery<Acteur> queryActeurs = em.createQuery(
        "SELECT a " +
            "FROM Film f " +
            "JOIN f.castingPrincipal a " +
            "WHERE f = :film " +
            "ORDER BY a.identite",
        Acteur.class);

    queryActeurs.setParameter("film", film);

    return queryActeurs.getResultList();
  }

  /**
   * Recherche les acteurs ayant interprété au moins un rôle
   * dans chacun des deux films donnés.
   *
   * @param premierFilm premier film recherché
   * @param secondFilm  second film recherché
   * @return la liste des acteurs communs aux deux films
   */
  public List<Acteur> findCommunsAuxFilms(
      Film premierFilm,
      Film secondFilm) {

    TypedQuery<Acteur> queryActeurs = em.createQuery(
        "SELECT r.acteur " +
            "FROM Role r " +
            "WHERE r.film IN (:premierFilm, :secondFilm) " +
            "GROUP BY r.acteur " +
            "HAVING COUNT(DISTINCT r.film) = 2 " +
            "ORDER BY r.acteur.identite",
        Acteur.class);

    queryActeurs.setParameter("premierFilm", premierFilm);
    queryActeurs.setParameter("secondFilm", secondFilm);

    return queryActeurs.getResultList();
  }

}