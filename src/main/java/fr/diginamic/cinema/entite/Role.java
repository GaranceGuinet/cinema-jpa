package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

/**
 * Représente un rôle référencé dans les données cinématographiques.
 */
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"film_id", "acteur_id", "personnage"}
    )
)
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Personnage interprété par l'acteur dans le film.
   */
  @Column(nullable = false)
  private String personnage;

  /**
   * Film auquel ce rôle appartient.
   */
  @ManyToOne
  @JoinColumn(name = "film_id", nullable = false)
  private Film film;

  /**
   * Acteur interprétant ce rôle.
   */
  @ManyToOne
  @JoinColumn(name = "acteur_id", nullable = false)
  private Acteur acteur;

  public Role() {
  }

  public Role(String personnage, Film film, Acteur acteur) {
    this.personnage = personnage;
    this.film = film;
    this.acteur = acteur;
  }

  /**
   * Compare ce rôle à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même rôle
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Role autre = (Role) objet;
    return id != null && id.equals(autre.id);
  }

  /**
   * Retourne un code de hachage stable pour cette entité.
   *
   * @return le code de hachage de la classe Hibernate réelle
   */
  @Override
  public int hashCode() {
    return Hibernate.getClass(this).hashCode();
  }

  @Override
  public String toString() {
    return "Role " +
        "id : " + id +
        ", personnage : " + personnage;
  }

  public Long getId() {
    return id;
  }

  public String getPersonnage() {
    return personnage;
  }

  public Film getFilm() {
    return film;
  }

  public Acteur getActeur() {
    return acteur;
  }

  public void setPersonnage(String personnage) {
    this.personnage = personnage;
  }

  public void setFilm(Film film) {
    this.film = film;
  }

  public void setActeur(Acteur acteur) {
    this.acteur = acteur;
  }
}
