# Décisions de conception

Ce document présente les principaux choix retenus lors de la conception du projet.

---

# Héritage Personne

Les acteurs et les réalisateurs partagent plusieurs informations communes :

- identifiant IMDb ;
- identité ;
- date de naissance ;
- URL.

Afin d'éviter toute duplication de ces informations, elles ont été regroupées dans une classe abstraite **Personne**.

Les classes **Acteur** et **Realisateur** héritent de cette classe.

Dans le modèle physique de données, cette conception est représentée par :

- une table PERSONNE ;
- une table ACTEUR dont la clé primaire est également une clé étrangère vers PERSONNE ;
- une table REALISATEUR construite selon le même principe.

Les fichiers fournis peuvent référencer une même personne à la fois comme acteur et comme réalisateur. Afin de conserver cette stratégie d'héritage, l'identifiant IMDb n'est pas déclaré unique dans la table PERSONNE. Deux occurrences distinctes peuvent ainsi être créées tout en partageant le même identifiant IMDb.

---

# Entité Role

Le personnage joué dans un film possède sa propre identité métier.

Il est donc représenté par une entité **Role** plutôt que par une simple table de jointure.

Un rôle :

- appartient à un seul film ;
- est associé à un seul acteur ;
- possède le nom du personnage interprété.

Cette modélisation permet à un même acteur d'interpréter plusieurs personnages dans un même film ou dans plusieurs films.

---

# Tables de jointure

Les relations plusieurs-à-plusieurs sont représentées par des tables de jointure :

- FILM_GENRE ;
- FILM_REALISATEUR ;
- CASTING_PRINCIPAL.

Les tables de jointure utilisent une clé primaire composite constituée des clés étrangères qu'elles relient.

---

# Lieu de naissance

Le lieu de naissance est modélisé comme une entité indépendante.

Une personne peut posséder au maximum un lieu de naissance.

Un lieu de naissance peut être associé à plusieurs personnes.

Cette modélisation évite de dupliquer les informations géographiques.

---

# Lieu de tournage

Le lieu de tournage est représenté par une entité dédiée.

Dans le modèle retenu :

- un film possède au maximum un lieu de tournage ;
- un lieu de tournage enregistré est associé à un seul film.

---

# Langue

La langue est modélisée comme une entité indépendante.

Dans le modèle retenu :

- un film possède au maximum une langue ;
- une langue peut être associée à plusieurs films.

---

# Pays

Le pays est modélisé comme une entité indépendante.

Dans le modèle retenu :

- un film possède au maximum un pays d'origine ;
- un pays peut être associé à plusieurs films.

---

# Genre

Le genre est modélisé comme une entité indépendante.

La relation entre Film et Genre est une relation plusieurs-à-plusieurs.

Cette conception évite la duplication des genres et facilite leur réutilisation.

---

# Identifiants

Toutes les entités persistées possèdent un identifiant technique de type **Long** généré automatiquement.

Les tables de jointure utilisent une clé primaire composite.

Les tables ACTEUR et REALISATEUR réutilisent la clé primaire de PERSONNE dans le cadre de la stratégie d'héritage **JOINED**.

---

# Contraintes d'intégrité

Le modèle met notamment en œuvre les contraintes suivantes :

- unicité de l'identifiant IMDb des films ;
- unicité du nom des genres ;
- unicité du nom des langues ;
- unicité du nom des pays ;
- unicité du nom des lieux de naissance ;
- unicité d'un rôle pour une combinaison (film, acteur, personnage) ;
- unicité de l'association entre un film et son lieu de tournage.

Les contraintes d'unicité garantissent l'absence de doublons sur les données de référence.

Les clés étrangères assurent la cohérence des relations entre les différentes entités.

---

# Hypothèses de conception

Certaines contraintes n'étant pas entièrement précisées dans les données fournies, les choix suivants ont été retenus lors de la conception :

- un film possède au maximum une langue ;
- un film possède au maximum un pays d'origine ;
- un film possède au maximum un lieu de tournage.

Ces choix sont appliqués de manière cohérente dans le diagramme de classes, le modèle physique de données et l'implémentation JPA.

---

# Objectif

L'objectif de cette conception est de produire un modèle :

- cohérent avec les données fournies ;
- conforme aux règles métier retenues ;
- facilement implémentable avec JPA ;
- évolutif pour les étapes suivantes du projet.