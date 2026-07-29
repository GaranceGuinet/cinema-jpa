package fr.diginamic.cinema.entite;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un réalisateur référencé dans les données cinématographiques.
 */
@Entity
@PrimaryKeyJoinColumn(name = "personne_id")
public class Realisateur extends Personne {

  /**
   * Ensemble des films réalisés par ce réalisateur.
   */
  @ManyToMany(mappedBy = "realisateurs")
  private Set<Film> films = new HashSet<>();

  public Realisateur() {
  }

  public Realisateur(String idImdb, String identite, LocalDate dateNaissance, String url, LieuNaissance lieuNaissance) {
    super(idImdb, identite, dateNaissance, url, lieuNaissance);
  }

  @Override
  public String toString() {
    return "Realisateur " + super.toString();
  }

  public Set<Film> getFilms() {
    return films;
  }
}
