package fr.diginamic.cinema.entite;


import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un lieu de naissance.
 * <p>
 * Un lieu de naissance peut être associé à plusieurs personnes.
 */
@Entity
@Table(name = "lieu_naissance")
public class LieuNaissance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Nom du lieu de naissance.
   */
  @Column(nullable = false, unique = true)
  private String nom;

  /**
   * Ensemble des personnes associées à ce lieu de naissance.
   */
  @OneToMany(mappedBy = "lieuNaissance")
  private Set<Personne> personnes = new HashSet<>();

  public LieuNaissance() {
  }

  public LieuNaissance(String nom) {
    this.nom = nom;
  }

  /**
   * Compare ce lieu de naissance à un autre objet en se basant sur son
   * identifiant persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même lieu de naissance
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    LieuNaissance autre = (LieuNaissance) objet;
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
    return "LieuNaissance " +
        "id : " + id +
        ", nom : " + nom;
  }

  /**
   * Ajoute une personne au lieu de naissance et associe ce lieu de naissance à la personne.
   *
   * @param personne la personne à associer au lieu de naissance
   */
  public void addPersonne(Personne personne) {
    if (personne == null) {
      return;
    }

    this.personnes.add(personne);
    personne.setLieuNaissance(this);
  }

  public Long getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public Set<Personne> getPersonnes() {
    return personnes;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}
