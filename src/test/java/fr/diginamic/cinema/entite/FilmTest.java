package fr.diginamic.cinema.entite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires des méthodes métier de l'entité Film,
 * notamment la gestion des relations bidirectionnelles.
 */
class FilmTest {

  @Test
  void testAddGenre() {
    Film film = new Film();
    Genre genre = new Genre();

    film.addGenre(genre);

    assertTrue(film.getGenres().contains(genre));
    assertTrue(genre.getFilms().contains(film));
  }

  @Test
  void testRemoveGenre() {
    Film film = new Film();
    Genre genre = new Genre();

    film.addGenre(genre);
    film.removeGenre(genre);

    assertFalse(film.getGenres().contains(genre));
    assertFalse(genre.getFilms().contains(film));
  }

  @Test
  void testAssocierLieuTournage() {
    Film film = new Film();
    LieuTournage lieu = new LieuTournage();

    film.associerLieuTournage(lieu);

    assertEquals(lieu, film.getLieuTournage());
    assertEquals(film, lieu.getFilm());
  }

  @Test
  void testAddRealisateur() {
    Film film = new Film();
    Realisateur realisateur = new Realisateur();

    film.addRealisateur(realisateur);

    assertTrue(film.getRealisateurs().contains(realisateur));
    assertTrue(realisateur.getFilms().contains(film));
  }

  @Test
  void testAddActeurCastingPrincipal() {
    Film film = new Film();
    Acteur acteur = new Acteur();

    film.addActeurCastingPrincipal(acteur);

    assertTrue(film.getCastingPrincipal().contains(acteur));
    assertTrue(acteur.getFilmsCasting().contains(film));
  }

  @Test
  void testAddRole() {
    Film film = new Film();
    Role role = new Role();

    film.addRole(role);

    assertTrue(film.getRoles().contains(role));
    assertEquals(film, role.getFilm());
  }

  @Test
  void testRemplacementLieuTournage() {
    Film film = new Film();
    LieuTournage premierLieu = new LieuTournage();
    LieuTournage nouveauLieu = new LieuTournage();

    film.associerLieuTournage(premierLieu);
    film.associerLieuTournage(nouveauLieu);

    assertNull(premierLieu.getFilm());
    assertEquals(nouveauLieu, film.getLieuTournage());
    assertEquals(film, nouveauLieu.getFilm());
  }

  @Test
  void testRemoveRealisateur() {
    Film film = new Film();
    Realisateur realisateur = new Realisateur();

    film.addRealisateur(realisateur);
    film.removeRealisateur(realisateur);

    assertFalse(film.getRealisateurs().contains(realisateur));
    assertFalse(realisateur.getFilms().contains(film));
  }

  @Test
  void testRemoveActeurCastingPrincipal() {
    Film film = new Film();
    Acteur acteur = new Acteur();

    film.addActeurCastingPrincipal(acteur);
    film.removeActeurCastingPrincipal(acteur);

    assertFalse(film.getCastingPrincipal().contains(acteur));
    assertFalse(acteur.getFilmsCasting().contains(film));
  }

  @Test
  void testSupprimerLieuTournage() {
    Film film = new Film();
    LieuTournage lieu = new LieuTournage();

    film.associerLieuTournage(lieu);
    film.associerLieuTournage(null);

    assertNull(film.getLieuTournage());
    assertNull(lieu.getFilm());
  }
}