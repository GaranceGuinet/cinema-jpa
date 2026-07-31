package fr.diginamic.cinema.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire permettant de lire les fichiers CSV
 * placés dans src/main/resources.
 */
public final class LecteurCsv {

  private LecteurCsv() {
  }

  /**
   * Ouvre un fichier CSV présent dans les ressources du projet.
   *
   * @param nomRessource chemin du fichier dans resources
   * @return parseur CSV à fermer avec un try-with-resources
   * @throws IOException si le fichier est introuvable ou illisible
   */
  public static CSVParser lireCsv(String nomRessource)
      throws IOException {

    InputStream flux = ouvrirRessource(nomRessource);

    Reader lecteur = new InputStreamReader(
        flux,
        StandardCharsets.UTF_8
    );

    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setDelimiter(';')
        .setHeader()
        .setSkipHeaderRecord(true)
        .setIgnoreEmptyLines(true)
        .setIgnoreSurroundingSpaces(true)
        .setAllowMissingColumnNames(true)
        .get();

    return format.parse(lecteur);
  }

  /**
   * Lit toutes les lignes d'un fichier présent dans les ressources.
   *
   * Cette méthode est utilisée pour films.csv, car certains résumés
   * contiennent des points-virgules non protégés par des guillemets.
   *
   * @param nomRessource chemin du fichier dans resources
   * @return toutes les lignes du fichier
   * @throws IOException si le fichier est introuvable ou illisible
   */
  public static List<String> lireLignes(String nomRessource)
      throws IOException {

    try (InputStream flux = ouvrirRessource(nomRessource);
         BufferedReader lecteur = new BufferedReader(
             new InputStreamReader(flux, StandardCharsets.UTF_8)
         )) {

      return lecteur.lines().collect(Collectors.toList());
    }
  }

  private static InputStream ouvrirRessource(String nomRessource)
      throws IOException {

    InputStream flux = LecteurCsv.class
        .getClassLoader()
        .getResourceAsStream(nomRessource);

    if (flux == null) {
      throw new IOException(
          "Ressource introuvable : " + nomRessource
      );
    }

    return flux;
  }
}