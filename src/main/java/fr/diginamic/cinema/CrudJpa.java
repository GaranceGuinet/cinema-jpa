package fr.diginamic.cinema;

import fr.diginamic.cinema.entite.Genre;
import fr.diginamic.cinema.service.GenreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Application console permettant de démontrer
 * les opérations CRUD sur l'entité Genre.
 */
public class CrudJpa {

  public static void main(String[] args) {

    EntityManagerFactory emf = null;
    EntityManager em = null;
    Scanner scanner = null;

    try {

      emf = Persistence.createEntityManagerFactory("cinema");
      em = emf.createEntityManager();
      scanner = new Scanner(System.in);

      GenreService genreService = new GenreService(em);

      boolean quitter = false;

      while (!quitter) {

        afficherMenu();

        String saisie = scanner.nextLine();

        try {

          int choix = Integer.parseInt(saisie);

          switch (choix) {

            case 1 -> creerGenre(scanner, genreService);

            case 2 -> consulterGenre(scanner, genreService);

            case 3 -> afficherTousLesGenres(genreService);

            case 4 -> modifierGenre(scanner, genreService);

            case 5 -> supprimerGenre(scanner, genreService);

            case 6 -> {
              quitter = true;
              System.out.println("Au revoir !");
            }

            default -> System.out.println("Choix invalide.");
          }

        } catch (NumberFormatException e) {

          System.out.println("Veuillez saisir un nombre valide.");

        } catch (IllegalArgumentException e) {

          System.out.println(e.getMessage());

        } catch (RuntimeException e) {

          System.out.println(
              "Une erreur est survenue : " + e.getMessage()
          );
        }
      }

    } finally {

      if (scanner != null) {
        scanner.close();
      }

      if (em != null && em.isOpen()) {
        em.close();
      }

      if (emf != null && emf.isOpen()) {
        emf.close();
      }
    }
  }

  /**
   * Affiche le menu principal.
   */
  private static void afficherMenu() {

    System.out.println();
    System.out.println("=================================");
    System.out.println(" Gestion des genres");
    System.out.println("=================================");
    System.out.println("1 - Créer un genre");
    System.out.println("2 - Consulter un genre");
    System.out.println("3 - Afficher tous les genres");
    System.out.println("4 - Modifier un genre");
    System.out.println("5 - Supprimer un genre");
    System.out.println("6 - Quitter");
    System.out.print("Votre choix : ");
  }

  /**
   * Crée un genre.
   *
   * @param scanner      scanner utilisé pour la saisie
   * @param genreService service des genres
   */
  private static void creerGenre(
      Scanner scanner,
      GenreService genreService
  ) {

    System.out.print("Nom du genre à créer : ");
    String nom = scanner.nextLine();

    Genre genre = genreService.creer(nom);

    System.out.println(
        "Genre créé avec succès : "
            + genre.getId()
            + " - "
            + genre.getNom()
    );
  }

  /**
   * Consulte un genre par son identifiant.
   *
   * @param scanner      scanner utilisé pour la saisie
   * @param genreService service des genres
   */
  private static void consulterGenre(
      Scanner scanner,
      GenreService genreService
  ) {

    Long id = demanderIdentifiant(scanner);

    Optional<Genre> genre = genreService.rechercherParId(id);

    if (genre.isPresent()) {

      System.out.println(
          "Genre trouvé : "
              + genre.get().getId()
              + " - "
              + genre.get().getNom()
      );

    } else {

      System.out.println(
          "Aucun genre trouvé avec l'identifiant " + id + "."
      );
    }
  }

  /**
   * Affiche tous les genres.
   *
   * @param genreService service des genres
   */
  private static void afficherTousLesGenres(
      GenreService genreService
  ) {

    List<Genre> genres = genreService.rechercherTous();

    if (genres.isEmpty()) {

      System.out.println("Aucun genre enregistré.");

      return;
    }

    System.out.println();
    System.out.println("Liste des genres :");

    for (Genre genre : genres) {

      System.out.println(
          genre.getId()
              + " - "
              + genre.getNom()
      );
    }
  }

  /**
   * Modifie le nom d'un genre.
   *
   * @param scanner      scanner utilisé pour la saisie
   * @param genreService service des genres
   */
  private static void modifierGenre(
      Scanner scanner,
      GenreService genreService
  ) {

    Long id = demanderIdentifiant(scanner);

    System.out.print("Nouveau nom du genre : ");
    String nouveauNom = scanner.nextLine();

    Genre genre = genreService.modifier(id, nouveauNom);

    System.out.println(
        "Genre modifié avec succès : "
            + genre.getId()
            + " - "
            + genre.getNom()
    );
  }

  /**
   * Supprime un genre.
   *
   * @param scanner      scanner utilisé pour la saisie
   * @param genreService service des genres
   */
  private static void supprimerGenre(
      Scanner scanner,
      GenreService genreService
  ) {

    Long id = demanderIdentifiant(scanner);

    System.out.print(
        "Confirmer la suppression du genre "
            + id
            + " ? (o/n) : "
    );

    String confirmation = scanner.nextLine();

    if (!confirmation.equalsIgnoreCase("o")) {

      System.out.println("Suppression annulée.");

      return;
    }

    genreService.supprimer(id);

    System.out.println("Genre supprimé avec succès.");
  }

  /**
   * Demande et convertit un identifiant.
   *
   * @param scanner scanner utilisé pour la saisie
   * @return identifiant saisi
   */
  private static Long demanderIdentifiant(Scanner scanner) {

    System.out.print("Identifiant du genre : ");

    return Long.parseLong(scanner.nextLine());
  }
}