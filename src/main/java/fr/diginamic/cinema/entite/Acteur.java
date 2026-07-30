package fr.diginamic.cinema.entite;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Représente un acteur référencé dans les données cinématographiques.
 */
@Entity
@PrimaryKeyJoinColumn(name = "personne_id")
public class Acteur extends Personne {

  /**
   * Taille de l'acteur en mètres.
   */
  private Double taille;

  /**
   * Ensemble des rôles interprétés par cet acteur.
   */
  @OneToMany(mappedBy = "acteur")
  private Set<Role> roles = new HashSet<>();


  /**
   * Ensemble des films dans lesquels cet acteur appartient au casting principal.
   */
  @ManyToMany(mappedBy = "castingPrincipal")
  private Set<Film> filmsCasting = new HashSet<>();

  public Acteur() {
  }

  public Acteur(String idImdb, String identite, LocalDate dateNaissance, String url, LieuNaissance lieuNaissance, Double taille) {
    super(idImdb, identite, dateNaissance, url, lieuNaissance);
    this.taille = taille;
  }

  @Override
  public String toString() {
    return "Acteur " + super.toString() +
        ", taille : " + taille;
  }

  /**
   * Ajoute un rôle à l'acteur et associe cet acteur au rôle.
   *
   * @param role le rôle à associer à l'acteur
   */
  public void addRole(Role role) {
    if (role == null) {
      return;
    }

    this.roles.add(role);
    role.setActeur(this);
  }

  public Double getTaille() {
    return taille;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public Set<Film> getFilmsCasting() {
    return filmsCasting;
  }

  public void setTaille(Double taille) {
    this.taille = taille;
  }
}
