package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

import fr.diginamic.cinema.entite.Acteur;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les films.
 */
public class FilmDao extends GenericDao<Film> {

  /**
   * Constructeur du DAO des films.
   *
   * @param em gestionnaire des entités
   */
  public FilmDao(EntityManager em) {
    super(em, Film.class);
  }

  /**
   * Recherche un film à partir de son identifiant IMDb.
   *
   * @param idImdb identifiant IMDB du film recherché
   * @return un Optional contenant le film s'il existe,
   * sinon un Optional vide
   */
  public Optional<Film> findByIdImdb(String idImdb) {

    TypedQuery<Film> queryFilm = em.createQuery(
        "SELECT f " +
            "FROM Film f " +
            "WHERE f.idImdb = :idImdb",
        Film.class);

    queryFilm.setParameter("idImdb", idImdb);

    List<Film> films = queryFilm.getResultList();

    return films.isEmpty()
        ? Optional.empty()
        : Optional.of(films.get(0));
  }

  /**
   * Recherche la filmographie d'un acteur à partir
   * des rôles qu'il a interprétés.
   *
   * @param acteur acteur dont la filmographie est recherchée
   * @return la liste des films dans lesquels l'acteur a interprété un rôle
   */
  public List<Film> findByActeur(Acteur acteur) {

    TypedQuery<Film> queryFilms = em.createQuery(
        "SELECT DISTINCT r.film " +
            "FROM Role r " +
            "WHERE r.acteur = :acteur " +
            "ORDER BY r.film.anneeDebut, r.film.nom",
        Film.class);

    queryFilms.setParameter("acteur", acteur);

    return queryFilms.getResultList();
  }

  /**
   * Recherche les films dont l'année de début est comprise
   * entre deux années données.
   *
   * @param anneeDebut année de début de la recherche
   * @param anneeFin   année de fin de la recherche
   * @return la liste des films correspondant à la période recherchée
   */
  public List<Film> findByPeriode(int anneeDebut, int anneeFin) {

    TypedQuery<Film> queryFilms = em.createQuery(
        "SELECT f " +
            "FROM Film f " +
            "WHERE f.anneeDebut BETWEEN :anneeDebut AND :anneeFin " +
            "ORDER BY f.anneeDebut, f.nom",
        Film.class);

    queryFilms.setParameter("anneeDebut", anneeDebut);
    queryFilms.setParameter("anneeFin", anneeFin);

    return queryFilms.getResultList();
  }

  /**
   * Recherche les films dans lesquels deux acteurs donnés
   * ont tous les deux interprété au moins un rôle.
   *
   * @param premierActeur premier acteur recherché
   * @param secondActeur  second acteur recherché
   * @return la liste des films communs aux deux acteurs
   */
  public List<Film> findCommunsAuxActeurs(
      Acteur premierActeur,
      Acteur secondActeur) {

    TypedQuery<Film> queryFilms = em.createQuery(
        "SELECT r.film " +
            "FROM Role r " +
            "WHERE r.acteur IN (:premierActeur, :secondActeur) " +
            "GROUP BY r.film " +
            "HAVING COUNT(DISTINCT r.acteur) = 2 " +
            "ORDER BY r.film.anneeDebut, r.film.nom",
        Film.class);

    queryFilms.setParameter("premierActeur", premierActeur);
    queryFilms.setParameter("secondActeur", secondActeur);

    return queryFilms.getResultList();
  }

  /**
   * Recherche les films sortis entre deux années données
   * dans lesquels un acteur donné a interprété au moins un rôle.
   *
   * @param acteur     acteur recherché
   * @param anneeDebut année de début de la recherche
   * @param anneeFin   année de fin de la recherche
   * @return la liste des films correspondant aux critères
   */
  public List<Film> findByActeurEtPeriode(
      Acteur acteur,
      int anneeDebut,
      int anneeFin) {

    TypedQuery<Film> queryFilms = em.createQuery(
        "SELECT DISTINCT r.film " +
            "FROM Role r " +
            "WHERE r.acteur = :acteur " +
            "AND r.film.anneeDebut BETWEEN :anneeDebut AND :anneeFin " +
            "ORDER BY r.film.anneeDebut, r.film.nom",
        Film.class);

    queryFilms.setParameter("acteur", acteur);
    queryFilms.setParameter("anneeDebut", anneeDebut);
    queryFilms.setParameter("anneeFin", anneeFin);

    return queryFilms.getResultList();
  }
}