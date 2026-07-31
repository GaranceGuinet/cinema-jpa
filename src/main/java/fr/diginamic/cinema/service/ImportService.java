package fr.diginamic.cinema.service;

import fr.diginamic.cinema.dao.*;
import fr.diginamic.cinema.entite.*;
import fr.diginamic.cinema.util.LecteurCsv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.text.Normalizer;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service chargé d'importer les données cinématographiques
 * contenues dans les fichiers CSV.
 */
public class ImportService {

  private static final String FICHIER_PAYS = "pays.csv";
  private static final String FICHIER_ACTEURS = "acteurs.csv";
  private static final String FICHIER_REALISATEURS = "realisateurs.csv";
  private static final String FICHIER_FILMS = "films.csv";
  private static final String FICHIER_ROLES = "roles.csv";
  private static final String FICHIER_CASTING = "castingPrincipal.csv";
  private static final String FICHIER_FILM_REALISATEURS =
      "film_realisateurs.csv";

  private static final DateTimeFormatter FORMAT_DATE_ANGLAIS =
      DateTimeFormatter.ofPattern(
          "MMMM d uuuu",
          Locale.ENGLISH
      );

  private static final DateTimeFormatter FORMAT_DATE_FRANCAIS =
      DateTimeFormatter.ofPattern(
          "d MMMM uuuu",
          Locale.FRENCH
      );

  private static final Pattern DATE_FRANCAISE_ABREGEE =
      Pattern.compile(
          "(\\d{1,2})-([A-Za-zÀ-ÿ]+)-(\\d{2})",
          Pattern.CASE_INSENSITIVE
      );

  private static final Map<String, Integer> MOIS_FRANCAIS =
      Map.ofEntries(
          Map.entry("janv", 1),
          Map.entry("févr", 2),
          Map.entry("fevr", 2),
          Map.entry("mars", 3),
          Map.entry("avr", 4),
          Map.entry("mai", 5),
          Map.entry("juin", 6),
          Map.entry("juil", 7),
          Map.entry("août", 8),
          Map.entry("aout", 8),
          Map.entry("sept", 9),
          Map.entry("oct", 10),
          Map.entry("nov", 11),
          Map.entry("déc", 12),
          Map.entry("dec", 12)
      );

  private static final Pattern TAILLE_METRES =
      Pattern.compile("(\\d+[.,]\\d+)\\s*m", Pattern.CASE_INSENSITIVE);

  private final EntityManager em;

  private final FilmDao filmDao;
  private final ActeurDao acteurDao;
  private final RealisateurDao realisateurDao;
  private final GenreDao genreDao;
  private final LangueDao langueDao;
  private final PaysDao paysDao;
  private final LieuNaissanceDao lieuNaissanceDao;
  private final LieuTournageDao lieuTournageDao;
  private final RoleDao roleDao;

  /**
   * Construit le service d'import.
   *
   * @param em gestionnaire d'entités partagé par les DAO
   */
  public ImportService(EntityManager em) {
    this.em = Objects.requireNonNull(em);

    this.filmDao = new FilmDao(em);
    this.acteurDao = new ActeurDao(em);
    this.realisateurDao = new RealisateurDao(em);
    this.genreDao = new GenreDao(em);
    this.langueDao = new LangueDao(em);
    this.paysDao = new PaysDao(em);
    this.lieuNaissanceDao = new LieuNaissanceDao(em);
    this.lieuTournageDao = new LieuTournageDao(em);
    this.roleDao = new RoleDao(em);
  }

  /**
   * Lance l'ensemble des imports dans l'ordre requis par les relations.
   *
   * @throws IOException si un fichier ne peut pas être lu
   */
  public void importerDonnees() throws IOException {

    importerPays("csv/" + FICHIER_PAYS);
    importerActeurs("csv/" + FICHIER_ACTEURS);
    importerRealisateurs("csv/" + FICHIER_REALISATEURS);
    importerFilms("csv/" + FICHIER_FILMS);
    importerRoles("csv/" + FICHIER_ROLES);
    importerCastingPrincipal("csv/" + FICHIER_CASTING);
    importerFilmRealisateurs(
        "csv/" + FICHIER_FILM_REALISATEURS
    );
  }

  /**
   * Importe les pays en garantissant leur unicité par leur nom.
   */
  private void importerPays(String ressource) throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Pays> paysConnus = paysDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              pays -> normaliser(pays.getNom()),
              pays -> pays,
              (premier, second) -> premier
          ));

      for (CSVRecord ligne : parser) {

        String nom = nettoyer(ligne.get("NOM"));
        String url = nettoyer(ligne.get("URL"));

        if (nom == null || url == null) {
          continue;
        }

        String cle = normaliser(nom);

        if (!paysConnus.containsKey(cle)) {
          Pays pays = new Pays(nom, url);
          paysDao.save(pays);
          paysConnus.put(cle, pays);
        }
      }

      transaction.commit();
      System.out.println("Pays importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les acteurs et leurs lieux de naissance.
   */
  private void importerActeurs(String ressource) throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Acteur> acteursConnus = acteurDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              Acteur::getIdImdb,
              acteur -> acteur,
              (premier, second) -> premier
          ));

      Map<String, LieuNaissance> lieuxConnus =
          chargerLieuxNaissance();

      for (CSVRecord ligne : parser) {

        String idImdb = nettoyer(ligne.get("ID IMDB"));

        if (idImdb == null || acteursConnus.containsKey(idImdb)) {
          continue;
        }

        String identite = nettoyer(ligne.get("IDENTITE"));
        String url = nettoyer(ligne.get("URL"));

        if (identite == null || url == null) {
          continue;
        }

        LieuNaissance lieu = obtenirOuCreerLieuNaissance(
            nettoyer(ligne.get("LIEU NAISSANCE")),
            lieuxConnus
        );

        Acteur acteur = new Acteur(
            idImdb,
            identite,
            convertirDate(ligne.get("DATE NAISSANCE")),
            url,
            lieu,
            convertirTaille(ligne.get("TAILLE"))
        );

        acteurDao.save(acteur);
        acteursConnus.put(idImdb, acteur);
      }

      transaction.commit();
      System.out.println("Acteurs importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les réalisateurs et leurs lieux de naissance.
   */
  private void importerRealisateurs(String ressource) throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Realisateur> realisateursConnus =
          realisateurDao.findAll()
              .stream()
              .collect(Collectors.toMap(
                  Realisateur::getIdImdb,
                  realisateur -> realisateur,
                  (premier, second) -> premier
              ));

      Map<String, LieuNaissance> lieuxConnus =
          chargerLieuxNaissance();

      for (CSVRecord ligne : parser) {

        String idImdb = nettoyer(ligne.get("ID"));

        if (idImdb == null
            || realisateursConnus.containsKey(idImdb)) {
          continue;
        }

        String identite = nettoyer(ligne.get("IDENTITE"));
        String url = nettoyer(ligne.get("URL"));

        if (identite == null || url == null) {
          continue;
        }

        LieuNaissance lieu = obtenirOuCreerLieuNaissance(
            nettoyer(ligne.get("LIEU NAISSANCE")),
            lieuxConnus
        );

        Realisateur realisateur = new Realisateur(
            idImdb,
            identite,
            convertirDate(ligne.get("DATE NAISSANCE")),
            url,
            lieu
        );

        realisateurDao.save(realisateur);
        realisateursConnus.put(idImdb, realisateur);
      }

      transaction.commit();
      System.out.println("Réalisateurs importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les films et crée les langues, genres et lieux de tournage.
   */
  private void importerFilms(String ressource) throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try {
      transaction.begin();

      Map<String, Film> filmsConnus = filmDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              Film::getIdImdb,
              film -> film,
              (premier, second) -> premier
          ));

      Map<String, Pays> paysConnus = paysDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              pays -> normaliser(pays.getNom()),
              pays -> pays,
              (premier, second) -> premier
          ));

      Map<String, Langue> languesConnues = langueDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              langue -> normaliser(langue.getNom()),
              langue -> langue,
              (premier, second) -> premier
          ));

      Map<String, Genre> genresConnus = genreDao.findAll()
          .stream()
          .collect(Collectors.toMap(
              genre -> normaliser(genre.getNom()),
              genre -> genre,
              (premier, second) -> premier
          ));

      List<String> lignes = LecteurCsv.lireLignes(ressource);

      for (int i = 1; i < lignes.size(); i++) {

        String[] colonnes = lignes.get(i).split(";", -1);

        if (colonnes.length < 10) {
          continue;
        }

        String idImdb = nettoyer(colonnes[0]);

        if (idImdb == null || filmsConnus.containsKey(idImdb)) {
          continue;
        }

        String nom = nettoyer(colonnes[1]);
        Integer[] annees = convertirAnnees(colonnes[2]);
        Double rating = convertirDouble(colonnes[3]);
        String url = nettoyer(colonnes[4]);
        String lieuTexte = nettoyer(colonnes[5]);
        String genresTexte = nettoyer(colonnes[6]);
        String langueTexte = nettoyer(colonnes[7]);

        String resume = nettoyer(String.join(
            ";",
            Arrays.copyOfRange(colonnes, 8, colonnes.length - 1)
        ));

        String paysTexte =
            nettoyer(colonnes[colonnes.length - 1]);

        if (nom == null || annees[0] == null || url == null) {
          continue;
        }

        Pays pays = paysTexte == null
            ? null
            : paysConnus.get(normaliser(paysTexte));

        Langue langue = obtenirOuCreerLangue(
            langueTexte,
            languesConnues
        );

        LieuTournage lieuTournage =
            creerLieuTournage(lieuTexte);

        Film film = new Film(
            idImdb,
            nom,
            annees[0],
            annees[1],
            resume,
            rating,
            url,
            langue,
            pays,
            lieuTournage
        );

        ajouterGenres(film, genresTexte, genresConnus);

        filmDao.save(film);
        filmsConnus.put(idImdb, film);
      }

      transaction.commit();
      System.out.println("Films importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les rôles en reliant films et acteurs.
   */
  private void importerRoles(String ressource) throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Film> films = chargerFilms();
      Map<String, Acteur> acteurs = chargerActeurs();

      Set<String> rolesConnus = roleDao.findAll()
          .stream()
          .map(role -> cleRole(
              role.getFilm().getIdImdb(),
              role.getActeur().getIdImdb(),
              role.getPersonnage()
          ))
          .collect(Collectors.toSet());

      for (CSVRecord ligne : parser) {

        String idFilm = nettoyer(ligne.get("FILM"));
        String idActeur = nettoyer(ligne.get("ID ACTEUR"));
        String personnage = nettoyer(ligne.get("PERSONNAGE"));

        Film film = films.get(idFilm);
        Acteur acteur = acteurs.get(idActeur);

        if (film == null || acteur == null || personnage == null) {
          continue;
        }

        String cle = cleRole(idFilm, idActeur, personnage);

        if (rolesConnus.contains(cle)) {
          continue;
        }

        Role role = new Role(personnage, film, acteur);
        roleDao.save(role);

        film.addRole(role);
        acteur.addRole(role);

        rolesConnus.add(cle);
      }

      transaction.commit();
      System.out.println("Rôles importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les associations du casting principal.
   */
  private void importerCastingPrincipal(String ressource)
      throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Film> films = chargerFilms();
      Map<String, Acteur> acteurs = chargerActeurs();

      for (CSVRecord ligne : parser) {

        Film film = films.get(nettoyer(ligne.get("FILM")));
        Acteur acteur =
            acteurs.get(nettoyer(ligne.get("ID ACTEUR")));

        if (film != null && acteur != null) {
          film.addActeurCastingPrincipal(acteur);
        }
      }

      transaction.commit();
      System.out.println("Casting principal importé.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  /**
   * Importe les associations entre films et réalisateurs.
   */
  private void importerFilmRealisateurs(String ressource)
      throws IOException {

    EntityTransaction transaction = em.getTransaction();

    try (CSVParser parser = LecteurCsv.lireCsv(ressource)) {

      transaction.begin();

      Map<String, Film> films = chargerFilms();

      Map<String, Realisateur> realisateurs =
          realisateurDao.findAll()
              .stream()
              .collect(Collectors.toMap(
                  Realisateur::getIdImdb,
                  realisateur -> realisateur,
                  (premier, second) -> premier
              ));

      for (CSVRecord ligne : parser) {

        Film film = films.get(nettoyer(ligne.get("FILM")));

        Realisateur realisateur = realisateurs.get(
            nettoyer(ligne.get("ID REALISATEUR"))
        );

        if (film != null && realisateur != null) {
          film.addRealisateur(realisateur);
        }
      }

      transaction.commit();
      System.out.println("Réalisateurs des films importés.");

    } catch (IOException | RuntimeException e) {
      annulerTransaction(transaction);
      throw e;
    }
  }

  private Map<String, Film> chargerFilms() {
    return filmDao.findAll()
        .stream()
        .collect(Collectors.toMap(
            Film::getIdImdb,
            film -> film,
            (premier, second) -> premier
        ));
  }

  private Map<String, Acteur> chargerActeurs() {
    return acteurDao.findAll()
        .stream()
        .collect(Collectors.toMap(
            Acteur::getIdImdb,
            acteur -> acteur,
            (premier, second) -> premier
        ));
  }

  private Map<String, LieuNaissance> chargerLieuxNaissance() {
    return lieuNaissanceDao.findAll()
        .stream()
        .collect(Collectors.toMap(
            lieu -> normaliser(lieu.getNom()),
            lieu -> lieu,
            (premier, second) -> premier
        ));
  }

  private LieuNaissance obtenirOuCreerLieuNaissance(
      String nom,
      Map<String, LieuNaissance> lieuxConnus) {

    if (nom == null) {
      return null;
    }

    String cle = normaliser(nom);
    LieuNaissance lieu = lieuxConnus.get(cle);

    if (lieu == null) {
      lieu = new LieuNaissance(nom);
      lieuNaissanceDao.save(lieu);
      lieuxConnus.put(cle, lieu);
    }

    return lieu;
  }

  private Langue obtenirOuCreerLangue(
      String nom,
      Map<String, Langue> languesConnues) {

    if (nom == null) {
      return null;
    }

    String cle = normaliser(nom);
    Langue langue = languesConnues.get(cle);

    if (langue == null) {
      langue = new Langue(nom);
      langueDao.save(langue);
      languesConnues.put(cle, langue);
    }

    return langue;
  }

  private void ajouterGenres(
      Film film,
      String genresTexte,
      Map<String, Genre> genresConnus) {

    if (genresTexte == null) {
      return;
    }

    for (String valeur : genresTexte.split(",")) {

      String nom = nettoyer(valeur);

      if (nom == null) {
        continue;
      }

      String cle = normaliser(nom);
      Genre genre = genresConnus.get(cle);

      if (genre == null) {
        genre = new Genre(nom);
        genreDao.save(genre);
        genresConnus.put(cle, genre);
      }

      film.addGenre(genre);
    }
  }

  private LieuTournage creerLieuTournage(String valeur) {

    if (valeur == null) {
      return null;
    }

    String[] parties = valeur.split(",", -1);

    String pays = parties.length > 0
        ? nettoyer(parties[0])
        : null;

    String etatDepartement = parties.length > 1
        ? nettoyer(parties[1])
        : null;

    String ville = parties.length > 2
        ? nettoyer(String.join(
        ",",
        Arrays.copyOfRange(parties, 2, parties.length)
    ))
        : null;

    if (pays == null) {
      return null;
    }

    LieuTournage lieu =
        new LieuTournage(ville, etatDepartement, pays);

    lieuTournageDao.save(lieu);

    return lieu;
  }

  private LocalDate convertirDate(String valeur) {

    String date = nettoyer(valeur);

    if (date == null) {
      return null;
    }

    date = date
        .replace(",", "")
        .replaceAll("\\s+", " ");

    try {
      return LocalDate.parse(date, FORMAT_DATE_ANGLAIS);
    } catch (DateTimeParseException ignored) {
      // La valeur ne correspond pas au format anglais complet.
    }

    try {
      return LocalDate.parse(
          date.toLowerCase(Locale.FRENCH),
          FORMAT_DATE_FRANCAIS
      );
    } catch (DateTimeParseException ignored) {
      // La valeur ne correspond pas au format français complet.
    }

    return convertirDateFrancaiseAbregee(date);
  }

  /**
   * Convertit les quelques dates françaises abrégées présentes
   * dans les fichiers CSV, par exemple 13-juil-42.
   * <p>
   * Les années sur deux chiffres des données sources appartiennent
   * au XXe siècle. Une valeur comme 07 devient donc 1907.
   *
   * @param valeur date à convertir
   * @return la date convertie, ou null si elle est incomplète ou invalide
   */
  private LocalDate convertirDateFrancaiseAbregee(String valeur) {

    Matcher matcher = DATE_FRANCAISE_ABREGEE.matcher(
        valeur.toLowerCase(Locale.FRENCH)
    );

    if (!matcher.matches()) {
      return null;
    }

    Integer mois = MOIS_FRANCAIS.get(matcher.group(2));

    if (mois == null) {
      return null;
    }

    int jour = Integer.parseInt(matcher.group(1));
    int annee = 1900 + Integer.parseInt(matcher.group(3));

    try {
      return LocalDate.of(annee, mois, jour);
    } catch (DateTimeException e) {
      return null;
    }
  }

  private Double convertirTaille(String valeur) {

    String taille = nettoyer(valeur);

    if (taille == null) {
      return null;
    }

    Matcher matcher = TAILLE_METRES.matcher(taille);

    if (!matcher.find()) {
      return null;
    }

    return convertirDouble(matcher.group(1));
  }

  private Integer[] convertirAnnees(String valeur) {

    String texte = nettoyer(valeur);
    Integer[] annees = {null, null};

    if (texte == null) {
      return annees;
    }

    Matcher matcher = Pattern.compile("\\d{4}").matcher(texte);

    if (matcher.find()) {
      annees[0] = Integer.valueOf(matcher.group());
    }

    if (matcher.find()) {
      annees[1] = Integer.valueOf(matcher.group());
    }

    return annees;
  }

  private Double convertirDouble(String valeur) {

    String texte = nettoyer(valeur);

    if (texte == null) {
      return null;
    }

    try {
      return Double.valueOf(texte.replace(',', '.'));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private String nettoyer(String valeur) {

    if (valeur == null) {
      return null;
    }

    String resultat = valeur
        .replace("\uFEFF", "")
        .trim();

    return resultat.isEmpty() ? null : resultat;
  }

  private String normaliser(String valeur) {

    if (valeur == null) {
      return null;
    }

    return Normalizer.normalize(valeur, Normalizer.Form.NFD)
        .replaceAll("\\p{M}+", "")
        .trim()
        .replaceAll("\\s+", " ")
        .toLowerCase(Locale.ROOT);
  }

  private String cleRole(
      String idFilm,
      String idActeur,
      String personnage) {

    return idFilm + "|" + idActeur + "|" + normaliser(personnage);
  }

  private void annulerTransaction(EntityTransaction transaction) {
    if (transaction.isActive()) {
      transaction.rollback();
    }
  }
}