package fr.diginamic.cinema.service;

import fr.diginamic.cinema.dao.GenreDao;
import fr.diginamic.cinema.entite.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

/**
 * Service permettant de réaliser les opérations CRUD
 * concernant les genres cinématographiques.
 */
public class GenreService {

  /**
   * Gestionnaire des entités.
   */
  private final EntityManager em;

  /**
   * DAO permettant l'accès aux genres.
   */
  private final GenreDao genreDao;

  /**
   * Constructeur du service.
   *
   * @param em gestionnaire des entités
   */
  public GenreService(EntityManager em) {
    this.em = em;
    this.genreDao = new GenreDao(em);
  }

  /**
   * Crée un nouveau genre.
   *
   * @param nom nom du genre à créer
   * @return genre créé
   */
  public Genre creer(String nom) {

    verifierNom(nom);

    Optional<Genre> genreExistant = genreDao.findByNom(nom.trim());

    if (genreExistant.isPresent()) {
      throw new IllegalArgumentException(
          "Un genre portant ce nom existe déjà."
      );
    }

    EntityTransaction transaction = em.getTransaction();

    try {
      transaction.begin();

      Genre genre = new Genre(nom.trim());
      genreDao.save(genre);

      transaction.commit();

      return genre;

    } catch (RuntimeException e) {

      if (transaction.isActive()) {
        transaction.rollback();
      }

      throw e;
    }
  }

  /**
   * Recherche un genre à partir de son identifiant.
   *
   * @param id identifiant du genre
   * @return genre trouvé, ou Optional vide
   */
  public Optional<Genre> rechercherParId(Long id) {

    if (id == null || id <= 0) {
      throw new IllegalArgumentException(
          "L'identifiant doit être strictement positif."
      );
    }

    return genreDao.findById(id);
  }

  /**
   * Recherche tous les genres.
   *
   * @return liste des genres
   */
  public List<Genre> rechercherTous() {
    return genreDao.findAll();
  }

  /**
   * Modifie le nom d'un genre.
   *
   * @param id         identifiant du genre
   * @param nouveauNom nouveau nom
   * @return genre modifié
   */
  public Genre modifier(Long id, String nouveauNom) {

    verifierNom(nouveauNom);

    Genre genre = rechercherParId(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "Aucun genre trouvé avec l'identifiant " + id + "."
        ));

    Optional<Genre> genrePortantCeNom =
        genreDao.findByNom(nouveauNom.trim());

    if (genrePortantCeNom.isPresent()
        && !genrePortantCeNom.get().getId().equals(id)) {

      throw new IllegalArgumentException(
          "Un autre genre porte déjà ce nom."
      );
    }

    EntityTransaction transaction = em.getTransaction();

    try {
      transaction.begin();

      genre.setNom(nouveauNom.trim());

      transaction.commit();

      return genre;

    } catch (RuntimeException e) {

      if (transaction.isActive()) {
        transaction.rollback();
      }

      throw e;
    }
  }

  /**
   * Supprime un genre à partir de son identifiant.
   *
   * @param id identifiant du genre à supprimer
   */
  public void supprimer(Long id) {

    Genre genre = rechercherParId(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "Aucun genre trouvé avec l'identifiant " + id + "."
        ));

    EntityTransaction transaction = em.getTransaction();

    try {
      transaction.begin();

      genreDao.delete(genre);

      transaction.commit();

    } catch (RuntimeException e) {

      if (transaction.isActive()) {
        transaction.rollback();
      }

      throw e;
    }
  }

  /**
   * Vérifie qu'un nom est exploitable.
   *
   * @param nom nom à contrôler
   */
  private void verifierNom(String nom) {

    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException(
          "Le nom du genre ne peut pas être vide."
      );
    }
  }
}