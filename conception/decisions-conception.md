Décisions de conception

Ce document présente les principaux choix retenus lors de la conception et de l'implémentation du projet.

Héritage Personne

Les acteurs et les réalisateurs partagent plusieurs informations communes : identifiant IMDb, identité, date de naissance, URL et lieu de naissance. Ces informations sont regroupées dans une classe abstraite Personne.

Les classes Acteur et Realisateur héritent de cette classe. L'implémentation JPA utilise la stratégie d'héritage JOINED, avec une table PERSONNE, une table ACTEUR et une table REALISATEUR.

Les fichiers sources pouvant référencer une même personne comme acteur et comme réalisateur, l'identifiant IMDb n'est pas déclaré unique dans PERSONNE.

Entité Role

Le personnage interprété dans un film possède une identité métier. Il est donc représenté par une entité Role.

Un rôle :

appartient à un seul film ;

est associé à un seul acteur ;

possède le nom du personnage interprété.

Une contrainte d'unicité est appliquée sur (film, acteur, personnage).

Tables de jointure

Les relations plusieurs-à-plusieurs sont représentées par :

FILM_GENRE ;

FILM_REALISATEUR ;

CASTING_PRINCIPAL.

Ces tables utilisent une clé primaire composite formée de leurs deux clés étrangères.

Lieu de naissance

Une personne possède au maximum un lieu de naissance.

Un lieu de naissance peut être associé à plusieurs personnes.

Le nom du lieu de naissance est unique.

Lieu de tournage

Un film possède au maximum un lieu de tournage.

Un lieu de tournage peut être associé à zéro ou un film.

La clé étrangère est portée par FILM et est unique.

Langue

Un film possède au maximum une langue.

Une langue peut être associée à plusieurs films.

Le nom de la langue est unique.

Pays

Un film possède au maximum un pays d'origine.

Un pays peut être associé à plusieurs films.

Le nom du pays est unique.

Genre

La relation entre Film et Genre est plusieurs-à-plusieurs. Le nom du genre est unique.

Identifiants et intégrité

Les entités principales possèdent un identifiant technique de type Long, généré automatiquement.

Le modèle applique notamment :

l'unicité de l'identifiant IMDb des films ;

l'unicité des noms de genre, langue, pays et lieu de naissance ;

l'unicité d'un rôle pour (film, acteur, personnage) ;

l'unicité de l'association entre un film et son lieu de tournage.

Architecture applicative

Le projet est structuré en couches :

entités : domaine et mapping JPA ;

DAO : accès aux données ;

services : logique métier et transactions ;

applications console : interaction avec l'utilisateur.

Applications principales :

InitialisationJpa : import des CSV ;

RechercheJpa : six recherches métier ;

CrudJpa : démonstration CRUD sur Genre.

Import des données

Les données de référence sont normalisées afin d'éviter les doublons liés à la casse, aux accents ou aux espaces superflus.

Chaque phase d'import est exécutée dans une transaction avec annulation en cas d'erreur.

Recherches métier

L'application permet :

la filmographie d'un acteur ;

le casting principal d'un film ;

les films sortis entre deux années ;

les films communs à deux acteurs ;

les acteurs communs à deux films ;

les films d'un acteur sortis entre deux années.

Les requêtes sont centralisées dans les DAO spécialisés et exposées par RechercheService.

CRUD

Une application dédiée permet de créer, consulter, afficher, modifier et supprimer un genre. Les transactions d'écriture sont gérées dans GenreService.

Hypothèses retenues

un film possède au maximum une langue ;

un film possède au maximum un pays d'origine ;

un film possède au maximum un lieu de tournage.

Ces choix sont appliqués de façon cohérente dans les diagrammes et l'implémentation JPA.