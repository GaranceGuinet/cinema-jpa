package fr.diginamic.cinema;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Initialise l'unité de persistance JPA afin de vérifier
 * la configuration, la connexion à la base de données
 * et le chargement des entités.
 */
public class InitialisationJpa {

  public static void main(String[] args){

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("cinema");

    emf.close();
  }
}
