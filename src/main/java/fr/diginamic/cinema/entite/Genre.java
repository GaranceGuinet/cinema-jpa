package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un genre cinématographique.
 * <p>
 * Un genre peut être associé à plusieurs films.
 */
@Entity
public class Genre {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nom du genre cinématographique.
   */
  @Column(nullable = false, unique = true)
  private String nom;

  /**
   * Ensemble des films appartenant à ce genre.
   */
  @ManyToMany(mappedBy = "genres")
  private Set<Film> films = new HashSet<>();

  public Genre() {
  }

  public Genre(String nom) {
    this.nom = nom;
  }

  /**
   * Compare ce genre à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même genre
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Genre autre = (Genre) objet;
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
    return "Genre " +
        "id : " + id +
        ", nom : " + nom;
  }

  public Long getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public Set<Film> getFilms() {
    return films;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}