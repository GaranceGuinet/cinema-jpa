package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.Role;
import jakarta.persistence.EntityManager;

import fr.diginamic.cinema.entite.Acteur;
import fr.diginamic.cinema.entite.Film;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les rôles.
 */
public class RoleDao extends GenericDao<Role> {

  /**
   * Constructeur du DAO des rôles.
   *
   * @param em gestionnaire des entités
   */
  public RoleDao(EntityManager em) {
    super(em, Role.class);
  }

  /**
   * Recherche un rôle à partir du film, de l'acteur
   * et du personnage interprété.
   *
   * @param film       film auquel le rôle appartient
   * @param acteur     acteur interprétant le rôle
   * @param personnage personnage interprété par l'acteur
   * @return un Optional contenant le rôle s'il existe,
   * sinon un Optional vide
   */
  public Optional<Role> findByFilmActeurEtPersonnage(
      Film film,
      Acteur acteur,
      String personnage) {

    TypedQuery<Role> queryRole = em.createQuery(
        "SELECT r " +
            "FROM Role r " +
            "WHERE r.film = :film " +
            "AND r.acteur = :acteur " +
            "AND r.personnage = :personnage",
        Role.class);

    queryRole.setParameter("film", film);
    queryRole.setParameter("acteur", acteur);
    queryRole.setParameter("personnage", personnage);

    List<Role> roles = queryRole.getResultList();

    return roles.isEmpty()
        ? Optional.empty()
        : Optional.of(roles.get(0));
  }
}