package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente une langue.
 * <p>
 * Une langue peut être associée à plusieurs films.
 */
@Entity
public class Langue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nom de la langue.
   */
  @Column(nullable = false, unique = true)
  private String nom;

  /**
   * Ensemble des films associés à cette langue.
   */
  @OneToMany(mappedBy = "langue")
  private Set<Film> films = new HashSet<>();

  public Langue() {
  }

  public Langue(String nom) {
    this.nom = nom;
  }

  /**
   * Compare cette langue à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent la même langue
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Langue autre = (Langue) objet;
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
    return "Langue " +
        "id : " + id +
        ", nom : " + nom;
  }

  /**
   * Ajoute un film à la langue et associe cette langue au film.
   *
   * @param film le film à associer à la langue
   */
  public void addFilm(Film film) {
    if (film == null) {
      return;
    }

    this.films.add(film);
    film.setLangue(this);
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
