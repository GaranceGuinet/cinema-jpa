package fr.diginamic.cinema;

import fr.diginamic.cinema.entite.Acteur;
import fr.diginamic.cinema.entite.Film;
import fr.diginamic.cinema.service.RechercheService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

/**
 * Application permettant d'effectuer les recherches
 * dans la base de données cinéma.
 */
public class RechercheJpa {

  public static void main(String[] args) {

    EntityManagerFactory emf = null;
    EntityManager em = null;
    Scanner scanner = null;

    try {

      emf = Persistence.createEntityManagerFactory("cinema");
      em = emf.createEntityManager();

      RechercheService rechercheService =
          new RechercheService(em);

      scanner = new Scanner(System.in);

      boolean quitter = false;

      while (!quitter) {

        System.out.println();
        System.out.println("=================================");
        System.out.println(" Base de données Cinéma");
        System.out.println("=================================");
        System.out.println("1 - Filmographie d'un acteur");
        System.out.println("2 - Casting d'un film");
        System.out.println("3 - Films entre deux années");
        System.out.println("4 - Films communs à deux acteurs");
        System.out.println("5 - Acteurs communs à deux films");
        System.out.println("6 - Films entre deux années avec un acteur");
        System.out.println("7 - Quitter");

        int choix = lireEntier(scanner, "Votre choix : ");

        switch (choix) {

          case 1 -> {

            System.out.print("Nom de l'acteur : ");
            String nom = scanner.nextLine();

            List<Acteur> acteurs =
                rechercheService.rechercherActeurs(nom);

            if (acteurs.isEmpty()) {

              System.out.println("Aucun acteur trouvé.");

            } else {

              Acteur acteur = choisirActeur(scanner, acteurs);

              List<Film> films =
                  rechercheService.rechercherFilmographie(acteur);

              System.out.println();
              System.out.println("Filmographie de "
                  + acteur.getIdentite());

              for (Film film : films) {

                System.out.println(
                    film.getAnneeDebut()
                        + " - "
                        + film.getNom());
              }
            }
          }

          case 2 -> {

            System.out.print("Nom du film : ");
            String nom = scanner.nextLine();

            List<Film> films =
                rechercheService.rechercherFilms(nom);

            if (films.isEmpty()) {

              System.out.println("Aucun film trouvé.");

            } else {

              Film film = choisirFilm(scanner, films);

              List<Acteur> acteurs =
                  rechercheService.rechercherCasting(film);

              if (acteurs.isEmpty()) {

                System.out.println(
                    "Aucun acteur du casting principal n'est renseigné pour ce film."
                );

              } else {

                System.out.println();
                System.out.println(
                    "Casting de " + film.getNom()
                );

                for (Acteur acteur : acteurs) {

                  System.out.println(
                      acteur.getIdentite()
                  );
                }
              }

            }

          }
          case 3 -> {

            int anneeDebut =
                lireEntier(scanner, "Année de début : ");

            int anneeFin =
                lireEntier(scanner, "Année de fin : ");

            if (anneeDebut > anneeFin) {
              System.out.println(
                  "L'année de début doit être inférieure ou égale à l'année de fin."
              );
              continue;
            }

            List<Film> films =
                rechercheService.rechercherFilmsParPeriode(
                    anneeDebut,
                    anneeFin
                );

            if (films.isEmpty()) {

              System.out.println(
                  "Aucun film trouvé pour cette période."
              );

            } else {

              System.out.println();
              System.out.println(
                  "Films sortis entre "
                      + anneeDebut
                      + " et "
                      + anneeFin
                      + " :"
              );

              for (Film film : films) {

                System.out.println(
                    film.getAnneeDebut()
                        + " - "
                        + film.getNom()
                );
              }
            }
          }

          case 4 -> {

            System.out.print(
                "Nom du premier acteur : "
            );
            String nomPremierActeur =
                scanner.nextLine();

            System.out.print(
                "Nom du second acteur : "
            );
            String nomSecondActeur =
                scanner.nextLine();

            List<Acteur> premiersActeurs =
                rechercheService.rechercherActeurs(
                    nomPremierActeur
                );

            List<Acteur> secondsActeurs =
                rechercheService.rechercherActeurs(
                    nomSecondActeur
                );

            if (premiersActeurs.isEmpty()
                || secondsActeurs.isEmpty()) {

              System.out.println(
                  "Au moins un des deux acteurs "
                      + "n'a pas été trouvé."
              );

            } else {

              Acteur premierActeur =
                  choisirActeur(scanner, premiersActeurs);

              Acteur secondActeur =
                  choisirActeur(scanner, secondsActeurs);

              List<Film> films =
                  rechercheService.rechercherFilmsCommuns(
                      premierActeur,
                      secondActeur
                  );

              if (films.isEmpty()) {

                System.out.println(
                    "Aucun film commun trouvé."
                );

              } else {

                System.out.println();
                System.out.println(
                    "Films communs à "
                        + premierActeur.getIdentite()
                        + " et "
                        + secondActeur.getIdentite()
                        + " :"
                );

                for (Film film : films) {

                  System.out.println(
                      film.getAnneeDebut()
                          + " - "
                          + film.getNom()
                  );
                }
              }
            }
          }

          case 5 -> {

            System.out.print(
                "Nom du premier film : "
            );
            String nomPremierFilm =
                scanner.nextLine();

            System.out.print(
                "Nom du second film : "
            );
            String nomSecondFilm =
                scanner.nextLine();

            List<Film> premiersFilms =
                rechercheService.rechercherFilms(
                    nomPremierFilm
                );

            List<Film> secondsFilms =
                rechercheService.rechercherFilms(
                    nomSecondFilm
                );

            if (premiersFilms.isEmpty()
                || secondsFilms.isEmpty()) {

              System.out.println(
                  "Au moins un des deux films "
                      + "n'a pas été trouvé."
              );

            } else {

              Film premierFilm =
                  choisirFilm(scanner, premiersFilms);

              Film secondFilm =
                  choisirFilm(scanner, secondsFilms);

              List<Acteur> acteurs =
                  rechercheService.rechercherActeursCommuns(
                      premierFilm,
                      secondFilm
                  );

              if (acteurs.isEmpty()) {

                System.out.println(
                    "Aucun acteur commun trouvé."
                );

              } else {

                System.out.println();
                System.out.println(
                    "Acteurs communs à "
                        + premierFilm.getNom()
                        + " et "
                        + secondFilm.getNom()
                        + " :"
                );

                for (Acteur acteur : acteurs) {

                  System.out.println(
                      acteur.getIdentite()
                  );
                }
              }
            }
          }
          case 6 -> {

            System.out.print("Nom de l'acteur : ");
            String nomActeur = scanner.nextLine();

            int anneeDebut =
                lireEntier(scanner, "Année de début : ");

            int anneeFin =
                lireEntier(scanner, "Année de fin : ");

            if (anneeDebut > anneeFin) {
              System.out.println(
                  "L'année de début doit être inférieure ou égale à l'année de fin."
              );
              continue;
            }

            List<Acteur> acteurs =
                rechercheService.rechercherActeurs(
                    nomActeur
                );

            if (acteurs.isEmpty()) {

              System.out.println(
                  "Aucun acteur trouvé."
              );

            } else {

              Acteur acteur = choisirActeur(scanner, acteurs);

              List<Film> films =
                  rechercheService
                      .rechercherFilmsParActeurEtPeriode(
                          acteur,
                          anneeDebut,
                          anneeFin
                      );

              if (films.isEmpty()) {

                System.out.println(
                    "Aucun film trouvé pour cet acteur "
                        + "sur cette période."
                );

              } else {

                System.out.println();
                System.out.println(
                    "Films de "
                        + acteur.getIdentite()
                        + " sortis entre "
                        + anneeDebut
                        + " et "
                        + anneeFin
                        + " :"
                );

                for (Film film : films) {

                  System.out.println(
                      film.getAnneeDebut()
                          + " - "
                          + film.getNom()
                  );
                }
              }
            }
          }

          case 7 -> {

            quitter = true;
            System.out.println("Au revoir !");
          }

          default -> System.out.println(
              "Choix invalide."
          );
        }
      }

    } catch (NumberFormatException e) {

      System.err.println(
          "Veuillez saisir un nombre valide."
      );

    } catch (RuntimeException e) {

      System.err.println(
          "Erreur lors du lancement de l'application : "
              + e.getMessage()
      );

      e.printStackTrace();

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

  private static Film choisirFilm(
      Scanner scanner,
      List<Film> films) {

    if (films.size() == 1) {
      return films.get(0);
    }

    System.out.println("Plusieurs films correspondent à cette recherche :");

    for (int i = 0; i < films.size(); i++) {
      Film film = films.get(i);

      System.out.println(
          (i + 1)
              + " - "
              + film.getNom()
              + " ("
              + film.getAnneeDebut()
              + ", IMDb : "
              + film.getIdImdb()
              + ")"
      );
    }

    int choix;

    do {
      choix = lireEntier(
          scanner,
          "Choisissez un film : "
      );

      if (choix < 1 || choix > films.size()) {
        System.out.println("Choix invalide.");
      }

    } while (choix < 1 || choix > films.size());

    return films.get(choix - 1);
  }

  private static Acteur choisirActeur(
      Scanner scanner,
      List<Acteur> acteurs) {

    if (acteurs.size() == 1) {
      return acteurs.get(0);
    }

    System.out.println("Plusieurs acteurs correspondent à cette recherche :");

    for (int i = 0; i < acteurs.size(); i++) {
      Acteur acteur = acteurs.get(i);

      System.out.println(
          (i + 1)
              + " - "
              + acteur.getIdentite()
              + " ("
              + acteur.getDateNaissance()
              + ", IMDb : "
              + acteur.getIdImdb()
              + ")"
      );
    }
    int choix;

    do {
      choix = lireEntier(
          scanner,
          "Choisissez un acteur : "
      );

      if (choix < 1 || choix > acteurs.size()) {
        System.out.println("Choix invalide.");
      }

    } while (choix < 1 || choix > acteurs.size());

    return acteurs.get(choix - 1);
  }

  private static int lireEntier(Scanner scanner, String message) {
    while (true) {
      System.out.print(message);
      String saisie = scanner.nextLine().trim();

      try {
        return Integer.parseInt(saisie);
      } catch (NumberFormatException e) {
        System.out.println("Veuillez saisir un nombre entier valide.");
      }
    }
  }
}