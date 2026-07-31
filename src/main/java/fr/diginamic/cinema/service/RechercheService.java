package fr.diginamic.cinema.service;

import fr.diginamic.cinema.dao.ActeurDao;
import fr.diginamic.cinema.dao.FilmDao;
import fr.diginamic.cinema.entite.Acteur;
import fr.diginamic.cinema.entite.Film;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;

/**
 * Service chargé des recherches dans la base de données
 * cinématographique.
 */
public class RechercheService {

  /**
   * Gestionnaire des entités.
   */
  private final EntityManager em;

  /**
   * DAO des films.
   */
  private final FilmDao filmDao;

  /**
   * DAO des acteurs.
   */
  private final ActeurDao acteurDao;

  /**
   * Construit le service de recherche.
   *
   * @param em gestionnaire d'entités partagé par les DAO
   */
  public RechercheService(EntityManager em) {

    this.em = Objects.requireNonNull(em);

    this.filmDao = new FilmDao(this.em);
    this.acteurDao = new ActeurDao(this.em);
  }

  /**
   * Recherche les acteurs correspondant à une identité.
   *
   * @param identite identité recherchée
   * @return la liste des acteurs correspondants
   */
  public List<Acteur> rechercherActeurs(String identite) {
    return acteurDao.findByIdentite(identite);
  }

  /**
   * Recherche les films correspondant à un nom.
   *
   * @param nom nom recherché
   * @return la liste des films correspondants
   */
  public List<Film> rechercherFilms(String nom) {
    return filmDao.findByNom(nom);
  }

  /**
   * Recherche la filmographie d'un acteur.
   *
   * @param acteur acteur recherché
   * @return la filmographie de l'acteur
   */
  public List<Film> rechercherFilmographie(Acteur acteur) {
    return filmDao.findByActeur(acteur);
  }

  /**
   * Recherche le casting principal d'un film.
   *
   * @param film film recherché
   * @return le casting principal du film
   */
  public List<Acteur> rechercherCasting(Film film) {
    return acteurDao.findByFilm(film);
  }

  /**
   * Recherche les films compris entre deux années.
   *
   * @param anneeDebut année de début
   * @param anneeFin   année de fin
   * @return les films correspondant à la période
   */
  public List<Film> rechercherFilmsParPeriode(
      int anneeDebut,
      int anneeFin) {

    return filmDao.findByPeriode(anneeDebut, anneeFin);
  }

  /**
   * Recherche les films communs à deux acteurs.
   *
   * @param premierActeur premier acteur
   * @param secondActeur  second acteur
   * @return les films communs
   */
  public List<Film> rechercherFilmsCommuns(
      Acteur premierActeur,
      Acteur secondActeur) {

    return filmDao.findCommunsAuxActeurs(
        premierActeur,
        secondActeur);
  }

  /**
   * Recherche les acteurs communs à deux films.
   *
   * @param premierFilm premier film
   * @param secondFilm  second film
   * @return les acteurs communs
   */
  public List<Acteur> rechercherActeursCommuns(
      Film premierFilm,
      Film secondFilm) {

    return acteurDao.findCommunsAuxFilms(
        premierFilm,
        secondFilm);
  }

  /**
   * Recherche les films d'un acteur sortis
   * entre deux années.
   *
   * @param acteur     acteur recherché
   * @param anneeDebut année de début
   * @param anneeFin   année de fin
   * @return les films correspondant aux critères
   */
  public List<Film> rechercherFilmsParActeurEtPeriode(
      Acteur acteur,
      int anneeDebut,
      int anneeFin) {

    return filmDao.findByActeurEtPeriode(
        acteur,
        anneeDebut,
        anneeFin);
  }
}