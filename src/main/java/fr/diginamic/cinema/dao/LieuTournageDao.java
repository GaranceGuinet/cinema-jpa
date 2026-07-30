package fr.diginamic.cinema.dao;

import fr.diginamic.cinema.entite.LieuTournage;
import jakarta.persistence.EntityManager;

/**
 * DAO permettant d'effectuer les opérations
 * d'accès aux données concernant les lieux de tournage.
 */
public class LieuTournageDao extends GenericDao<LieuTournage> {

  /**
   * Constructeur du DAO des lieux de tournage.
   *
   * @param em gestionnaire des entités
   */
  public LieuTournageDao(EntityManager em) {
    super(em, LieuTournage.class);
  }
}