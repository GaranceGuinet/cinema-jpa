package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;


/**
 * Représente une personne référencée dans les données cinématographiques.
 * <p>
 * Cette classe abstraite regroupe les informations communes aux différentes
 * catégories de personnes du domaine.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Identifiant de la personne dans la base IMDB.
   */
  @Column(name = "id_imdb", nullable = false, length = 15)
  private String idImdb;

  /**
   * Nom complet ou identité sous laquelle la personne est référencée.
   */
  @Column(nullable = false)
  private String identite;

  @Column(name = "date_naissance")
  private LocalDate dateNaissance;

  /**
   * URL associée à la personne dans les données importées.
   */
  @Column(nullable = false)
  private String url;

  @ManyToOne
  @JoinColumn(name = "lieu_naissance_id")
  private LieuNaissance lieuNaissance;

  protected Personne() {
  }

  protected Personne(String idImdb, String identite, LocalDate dateNaissance, String url, LieuNaissance lieuNaissance) {
    this.idImdb = idImdb;
    this.identite = identite;
    this.dateNaissance = dateNaissance;
    this.url = url;
    this.lieuNaissance = lieuNaissance;
  }

  /**
   * Compare cette personne à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent la même personne
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Personne autre = (Personne) objet;
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
    return
        "id : " + id +
            ", idImdb : " + idImdb +
            ", identite : " + identite +
            ", dateNaissance : " + dateNaissance +
            ", url : " + url;
  }

  public Long getId() {
    return id;
  }

  public String getIdImdb() {
    return idImdb;
  }

  public String getIdentite() {
    return identite;
  }

  public LocalDate getDateNaissance() {
    return dateNaissance;
  }

  public String getUrl() {
    return url;
  }

  public LieuNaissance getLieuNaissance() {
    return lieuNaissance;
  }

  public void setIdImdb(String idImdb) {
    this.idImdb = idImdb;
  }

  public void setIdentite(String identite) {
    this.identite = identite;
  }

  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setLieuNaissance(LieuNaissance lieuNaissance) {
    this.lieuNaissance = lieuNaissance;
  }
}