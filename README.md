Cinéma JPA

Projet Java réalisé dans le cadre de la formation Concepteur Développeur d'Applications (CDA).

L'application importe des données cinématographiques depuis des fichiers CSV, les enregistre dans MariaDB avec JPA/Hibernate, puis permet de les exploiter à travers des applications console.

Fonctionnalités

Import

InitialisationJpa importe :

les pays ;

les acteurs ;

les réalisateurs ;

les films ;

les rôles ;

le casting principal ;

les associations entre films et réalisateurs.

Les genres, langues, lieux de naissance et lieux de tournage sont créés à partir des données importées.

Recherches

RechercheJpa propose :

filmographie d'un acteur ;

casting principal d'un film ;

films sortis entre deux années ;

films communs à deux acteurs ;

acteurs communs à deux films ;

films d'un acteur sortis entre deux années ;

fin de l'application.

CRUD

CrudJpa démontre les opérations CRUD sur l'entité Genre :

création ;

consultation ;

modification ;

suppression.

Technologies

Java 21

Maven

Jakarta Persistence 3.1

Hibernate ORM

MariaDB

Apache Commons CSV

Logback

JUnit

Architecture

src/main/java/fr/diginamic/cinema
├── dao
├── entite
├── service
├── util
├── InitialisationJpa.java
├── RechercheJpa.java
└── CrudJpa.java

Prérequis

Java 21

Maven

MariaDB

une base nommée cinema

Configuration

Le fichier JPA se trouve dans :

src/main/resources/META-INF/persistence.xml

Configuration par défaut :

URL : jdbc:mariadb://localhost:3306/cinema
Utilisateur : root
Mot de passe : vide

Le schéma doit exister avant le lancement de l'import, car la génération automatique est désactivée.

Lancement

Initialiser la base

fr.diginamic.cinema.InitialisationJpa

Lancer les recherches

fr.diginamic.cinema.RechercheJpa

Tester le CRUD

fr.diginamic.cinema.CrudJpa

Données sources

Les CSV sont placés dans :

src/main/resources/csv

Fichiers utilisés :

films.csv

acteurs.csv

realisateurs.csv

roles.csv

film_realisateurs.csv

castingPrincipal.csv

pays.csv

Conception

Le dossier conception contient :

le diagramme de classes ;

le modèle physique de données ;

les décisions de conception ;

le projet Visual Paradigm.

État de la version 1

La version 1 comprend :

l'import CSV ;

les entités JPA ;

la couche DAO ;

la couche Service ;

les six recherches demandées ;

le menu console ;

une démonstration CRUD ;

la documentation de conception.

Améliorations prévues

meilleure gestion des erreurs de saisie ;

gestion des homonymes ;

validation stricte des périodes ;

réduction des logs Hibernate ;

ajout de tests automatisés ;

finalisation de certaines DAO complémentaires.