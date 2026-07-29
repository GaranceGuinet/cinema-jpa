package fr.diginamic.cinema.entite;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un film référencé dans les données cinématographiques.
 */
@Entity
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Identifiant unique du film dans la base IMDB.
   */
  @Column(name = "id_imdb", nullable = false, unique = true, length = 15)
  private String idImdb;

  /**
   * Nom du film.
   */
  @Column(nullable = false)
  private String nom;

  /**
   * Année de sortie ou de début de diffusion du film.
   */
  @Column(name = "annee_debut", nullable = false)
  private Integer anneeDebut;

  /**
   * Année de fin de diffusion lorsque le film est une série.
   */
  @Column(name = "annee_fin")
  private Integer anneeFin;

  /**
   * Résumé du film.
   */
  @Column(length = 2000)
  private String resume;

  /**
   * Note moyenne du film.
   */
  @Column(precision = 3, scale = 1)
  private Double rating;

  /**
   * URL associée au film dans les données importées.
   */
  @Column(nullable = false)
  private String url;

  @ManyToOne
  @JoinColumn(name = "langue_id")
  private Langue langue;

  @ManyToOne
  @JoinColumn(name = "pays_id")
  private Pays pays;

  @OneToOne
  @JoinColumn(name = "lieu_tournage_id", unique = true)
  private LieuTournage lieuTournage;

  /**
   * Ensemble des rôles associés à ce film.
   */
  @OneToMany(mappedBy = "film")
  private Set<Role> roles = new HashSet<>();

  /**
   * Ensemble des réalisateurs de ce film.
   */
  @ManyToMany
  @JoinTable(
      name = "film_realisateur",
      joinColumns = @JoinColumn(name = "film_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "realisateur_id", nullable = false)
  )
  private Set<Realisateur> realisateurs = new HashSet<>();

  /**
   * Ensemble des genres associés à ce film.
   */
  @ManyToMany
  @JoinTable(
      name = "film_genre",
      joinColumns = @JoinColumn(name = "film_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "genre_id", nullable = false)
  )
  private Set<Genre> genres = new HashSet<>();

  /**
   * Ensemble des acteurs appartenant au casting principal de ce film.
   */
  @ManyToMany
  @JoinTable(
      name = "casting_principal",
      joinColumns = @JoinColumn(name = "film_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "acteur_id", nullable = false)
  )
  private Set<Acteur> castingPrincipal = new HashSet<>();

  public Film() {
  }

  public Film(String idImdb, String nom, Integer anneeDebut, Integer anneeFin, String resume, Double rating, String url, Langue langue, Pays pays, LieuTournage lieuTournage) {
    this.idImdb = idImdb;
    this.nom = nom;
    this.anneeDebut = anneeDebut;
    this.anneeFin = anneeFin;
    this.resume = resume;
    this.rating = rating;
    this.url = url;
    this.langue = langue;
    this.pays = pays;
    this.lieuTournage = lieuTournage;
  }

  /**
   * Compare ce film à un autre objet en se basant sur son identifiant
   * persistant.
   *
   * @param objet l'objet à comparer
   * @return {@code true} si les deux objets représentent le même film
   */
  @Override
  public boolean equals(Object objet) {
    if (this == objet) {
      return true;
    }
    if (objet == null || Hibernate.getClass(this) != Hibernate.getClass(objet)) {
      return false;
    }
    Film autre = (Film) objet;
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
    return "Film " +
        "id : " + id +
        ", idImdb : " + idImdb +
        ", nom : " + nom +
        ", anneeDebut : " + anneeDebut +
        ", anneeFin : " + anneeFin +
        ", resume : " + resume +
        ", rating : " + rating +
        ", url : " + url;
  }

  /**
   * Ajoute un genre au film et met à jour la relation bidirectionnelle
   * avec le genre.
   *
   * @param genre le genre à associer au film
   */
  public void addGenre(Genre genre) {
    if (genre == null) {
      return;
    }

    this.genres.add(genre);
    genre.getFilms().add(this);
  }

  /**
   * Supprime un genre du film et met à jour la relation bidirectionnelle
   * avec le genre.
   *
   * @param genre le genre à dissocier du film
   */
  public void removeGenre(Genre genre) {
    if (genre == null) {
      return;
    }

    this.genres.remove(genre);
    genre.getFilms().remove(this);
  }

  /**
   * Ajoute un réalisateur au film et met à jour la relation bidirectionnelle
   * avec le réalisateur.
   *
   * @param realisateur le réalisateur à associer au film
   */
  public void addRealisateur(Realisateur realisateur) {
    if (realisateur == null) {
      return;
    }

    this.realisateurs.add(realisateur);
    realisateur.getFilms().add(this);
  }

  /**
   * Supprime un réalisateur du film et met à jour la relation bidirectionnelle
   * avec le réalisateur.
   *
   * @param realisateur le réalisateur à dissocier du film
   */
  public void removeRealisateur(Realisateur realisateur) {
    if (realisateur == null) {
      return;
    }

    this.realisateurs.remove(realisateur);
    realisateur.getFilms().remove(this);
  }

  /**
   * Ajoute un acteur au casting principal du film et met à jour
   * la relation bidirectionnelle avec l’acteur.
   *
   * @param acteur l’acteur à associer au casting principal du film
   */
  public void addActeurCastingPrincipal(Acteur acteur) {
    if (acteur == null) {
      return;
    }

    this.castingPrincipal.add(acteur);
    acteur.getFilmsCasting().add(this);
  }

  /**
   * Supprime un acteur du casting principal du film et met à jour
   * la relation bidirectionnelle avec l’acteur.
   *
   * @param acteur l’acteur à dissocier du casting principal du film
   */
  public void removeActeurCastingPrincipal(Acteur acteur) {
    if (acteur == null) {
      return;
    }

    this.castingPrincipal.remove(acteur);
    acteur.getFilmsCasting().remove(this);
  }

  /**
   * Ajoute un rôle au film et associe ce film au rôle.
   *
   * @param role le rôle à associer au film
   */
  public void addRole(Role role) {
    if (role == null) {
      return;
    }

    this.roles.add(role);
    role.setFilm(this);
  }


  public Long getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public String getIdImdb() {
    return idImdb;
  }

  public Integer getAnneeDebut() {
    return anneeDebut;
  }

  public Integer getAnneeFin() {
    return anneeFin;
  }

  public String getResume() {
    return resume;
  }

  public Double getRating() {
    return rating;
  }

  public String getUrl() {
    return url;
  }

  public Langue getLangue() {
    return langue;
  }

  public Pays getPays() {
    return pays;
  }

  public LieuTournage getLieuTournage() {
    return lieuTournage;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public Set<Realisateur> getRealisateurs() {
    return realisateurs;
  }

  public Set<Genre> getGenres() {
    return genres;
  }

  public Set<Acteur> getCastingPrincipal() {
    return castingPrincipal;
  }

  public void setPays(Pays pays) {
    this.pays = pays;
  }

  public void setIdImdb(String idImdb) {
    this.idImdb = idImdb;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setAnneeDebut(Integer anneeDebut) {
    this.anneeDebut = anneeDebut;
  }

  public void setAnneeFin(Integer anneeFin) {
    this.anneeFin = anneeFin;
  }

  public void setResume(String resume) {
    this.resume = resume;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setLangue(Langue langue) {
    this.langue = langue;
  }

  public void setLieuTournage(LieuTournage lieuTournage) {
    this.lieuTournage = lieuTournage;
  }

}