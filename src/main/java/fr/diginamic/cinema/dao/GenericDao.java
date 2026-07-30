package fr.diginamic.cinema.dao;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * Classe générique permettant d'effectuer les opérations communes
 * d'accès aux données pour les entités du projet.
 *
 * @param <T> type de l'entité manipulée
 */
public abstract class GenericDao<T> {

  /**
   * Gestionnaire des entités.
   */
  protected final EntityManager em;

  /**
   * Classe de l'entité manipulée.
   */
  private final Class<T> entityClass;

  /**
   * Constructeur du DAO générique.
   *
   * @param em          gestionnaire des entités
   * @param entityClass classe de l'entité manipulée
   */
  protected GenericDao(EntityManager em, Class<T> entityClass) {
    this.em = em;
    this.entityClass = entityClass;
  }

  /**
   * Enregistre une nouvelle entité dans la base de données.
   *
   * @param entity entité à enregistrer
   */
  public void save(T entity) {
    em.persist(entity);
  }

  /**
   * Recherche une entité à partir de son identifiant.
   *
   * @param id identifiant de l'entité
   * @return un Optional contenant l'entité si elle existe,
   * sinon un Optional vide
   */
  public Optional<T> findById(Long id) {
    return Optional.ofNullable(em.find(entityClass, id));
  }

  /**
   * Recherche toutes les entités du type concerné.
   *
   * @return liste des entités trouvées
   */
  public List<T> findAll() {
    return em.createQuery(
        "SELECT e FROM " + entityClass.getSimpleName() + " e",
        entityClass
    ).getResultList();
  }

  /**
   * Supprime une entité gérée par le contexte de persistance.
   *
   * @param entity entité à supprimer
   * @throws IllegalArgumentException si l'entité n'est pas gérée
   *                                  par l'EntityManager
   */
  public void delete(T entity) {
    if (!em.contains(entity)) {
      throw new IllegalArgumentException(
          "L'entité doit être gérée avant d'être supprimée."
      );
    }

    em.remove(entity);
  }
}