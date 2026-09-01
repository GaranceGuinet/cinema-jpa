package fr.diginamic.cinema.entite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitaires des méthodes métier de l'entité Acteur,
 * notamment la gestion de ses relations.
 */
class ActeurTest {

  @Test
  void testAddRole() {
    Acteur acteur = new Acteur();
    Role role = new Role();

    acteur.addRole(role);

    assertTrue(acteur.getRoles().contains(role));
    assertEquals(acteur, role.getActeur());
  }
}