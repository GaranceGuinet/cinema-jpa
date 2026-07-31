package fr.diginamic.cinema;

import fr.diginamic.cinema.service.ImportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;

/**
 * Initialise JPA et lance l'import des fichiers CSV.
 */
public class InitialisationJpa {

  public static void main(String[] args) {

    EntityManagerFactory emf = null;
    EntityManager em = null;

    try {
      emf = Persistence.createEntityManagerFactory("cinema");
      em = emf.createEntityManager();

      ImportService importService = new ImportService(em);
      importService.importerDonnees();

      System.out.println("Initialisation terminée.");

    } catch (IOException e) {
      System.err.println(
          "Erreur pendant la lecture des fichiers CSV : "
              + e.getMessage()
      );

    } catch (RuntimeException e) {
      System.err.println(
          "Erreur pendant l'import en base de données : "
              + e.getMessage()
      );
      e.printStackTrace();

    } finally {
      if (em != null && em.isOpen()) {
        em.close();
      }

      if (emf != null && emf.isOpen()) {
        emf.close();
      }
    }
  }
}