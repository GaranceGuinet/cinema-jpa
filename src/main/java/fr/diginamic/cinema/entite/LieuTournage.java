package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

/**
 * Représente un lieu de tournage.
 * <p>
 * Un lieu de tournage peut être associé à un seul film.
 */
@Entity
@Table(name = "lieu_tournage")
public class LieuTournage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ville;

  @Column(name = "etat_departement")
  private String etatDepartement;

  @Column(nullable = false)
  private String pays;

  /**
   * Film associé à ce lieu de tournage.
   */
  @OneToOne(mappedBy = "lieuTournage")
  private Film film;

  public LieuTournage() {
  }

  public LieuTournage(String ville, String etatDepartement, String pays) {
    this.ville = ville;
    this.etatDepartement = etatDepartement;
    this.pays = pays;
  }

  /**
   * Compare ce lieu de tournage à un autre objet en se basant sur son
   * identifiant persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même lieu de tournage
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    LieuTournage autre = (LieuTournage) objet;
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
    return "LieuTournage " +
        "id : " + id +
        ", ville : " + ville +
        ", etatDepartement : " + etatDepartement +
        ", pays : " + pays;
  }


  public Long getId() {
    return id;
  }

  public String getVille() {
    return ville;
  }

  public String getEtatDepartement() {
    return etatDepartement;
  }

  public String getPays() {
    return pays;
  }

  public Film getFilm() {
    return film;
  }

  public void setEtatDepartement(String etatDepartement) {
    this.etatDepartement = etatDepartement;
  }

  public void setPays(String pays) {
    this.pays = pays;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }

  public void setFilm(Film film) {
    this.film = film;
  }
}
