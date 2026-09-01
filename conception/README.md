# Dossier de conception

Ce dossier regroupe les documents de conception du projet **Cinéma JPA**, réalisé dans le cadre de la formation
Concepteur Développeur d'Applications (CDA).

## Contenu

- `01-diagramme-de-classes.png` : diagramme de classes UML ;
- `02-modele-physique-donnees.png` : modèle physique de données ;
- `decisions-conception.md` : justification des principaux choix de modélisation ;
- `sources/cinema-conception.vpp` : fichier Visual Paradigm utilisé pour construire les diagrammes.

## Modèle retenu

Le projet repose notamment sur :

- une classe abstraite `Personne` ;
- les spécialisations `Acteur` et `Realisateur` ;
- une entité `Role` reliant un acteur, un film et un personnage ;
- une relation plusieurs-à-plusieurs entre `Film` et `Genre` ;
- une relation plusieurs-à-plusieurs entre `Film` et `Realisateur` ;
- une relation plusieurs-à-plusieurs entre `Film` et `Acteur` pour le casting principal ;
- une relation plusieurs-à-un entre `Film` et `Langue` ;
- une relation plusieurs-à-un entre `Film` et `Pays` ;
- une relation un-à-un entre `Film` et `LieuTournage` ;
- une relation plusieurs-à-un entre `Personne` et `LieuNaissance`.

## Cohérence avec l'implémentation

La conception est implémentée avec Jakarta Persistence et Hibernate.

Les entités JPA correspondent au modèle présenté dans les diagrammes, notamment pour :

- la stratégie d'héritage `JOINED` ;
- les tables de jointure ;
- les clés étrangères ;
- les contraintes d'unicité ;
- les cardinalités retenues.

## État actuel du projet

Le projet comprend :

- l'import des données CSV vers MariaDB ;
- une couche DAO ;
- une couche Service ;
- une application d'initialisation ;
- une application de recherche avec six recherches métier ;
- une application CRUD sur l'entité `Genre` ;
- la gestion des saisies utilisateur invalides ;
- la gestion des acteurs et films homonymes ;
- des tests unitaires sur plusieurs relations entre entités.