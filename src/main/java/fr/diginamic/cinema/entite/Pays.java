package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un pays.
 * <p>
 * Un pays peut être associé à plusieurs films.
 */
@Entity
public class Pays {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nom du pays.
   */
  @Column(nullable = false, unique = true)
  private String nom;

  /**
   * URL associée au pays dans les données importées.
   */
  @Column(nullable = false)
  private String url;

  /**
   * Ensemble des films associés à ce pays.
   */
  @OneToMany(mappedBy = "pays")
  private Set<Film> films = new HashSet<>();

  public Pays() {
  }

  public Pays(String nom, String url) {
    this.nom = nom;
    this.url = url;
  }

  /**
   * Compare ce pays à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même pays
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Pays autre = (Pays) objet;
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
    return "Pays " +
        "id : " + id +
        ", nom : " + nom +
        ", url : " + url;
  }

  /**
   * Ajoute un film au pays et associe ce pays au film.
   *
   * @param film le film à associer au pays
   */
  public void addFilm(Film film) {
    if (film == null) {
      return;
    }

    this.films.add(film);
    film.setPays(this);
  }

  public Long getId() {
    return id;
  }

  public String getUrl() {
    return url;
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

  public void setUrl(String url) {
    this.url = url;
  }
}
