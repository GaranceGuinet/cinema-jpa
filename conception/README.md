# Projet Cinéma – CDA

## Présentation

Ce projet est réalisé dans le cadre de la formation **Concepteur Développeur d'Applications (CDA)**.

L'objectif est de concevoir puis développer une application Java permettant de construire puis d'exploiter une base de données de films à partir de plusieurs fichiers CSV et JSON.

Le projet est développé avec :

- Java
- Maven
- Jakarta Persistence (JPA)
- Hibernate
- MariaDB

---

## Objectifs

Le projet est construit en plusieurs étapes :

1. analyse des données fournies ;
2. conception UML ;
3. réalisation du modèle physique de données ;
4. implémentation des entités JPA ;
5. développement de la couche DAO ;
6. développement de la couche Service ;
7. réalisation des applications demandées.

---

## Dossier conception

Le dossier **conception** regroupe l'ensemble des documents ayant servi à la modélisation du projet.

Il contient :

- le diagramme de classes UML ;
- le modèle physique de données (MPD) ;
- le projet Visual Paradigm (.vpp) ;
- un document présentant les principaux choix de conception.

Ces documents servent de référence afin de garantir la cohérence entre la conception et l'implémentation.

Le projet a été conçu en suivant une démarche de modélisation UML avant toute implémentation.

---

## Données

Les données sont fournies sous la forme de fichiers CSV et JSON représentant notamment :

- les films ;
- les acteurs ;
- les réalisateurs ;
- les rôles ;
- les associations entre films et réalisateurs ;
- les pays.

Ces données sont importées afin d'alimenter la base de données relationnelle.

Les entités **Genre**, **Langue**, **LieuNaissance** et **LieuTournage** sont construites à partir des informations présentes dans les données importées.

---

## État d'avancement

- ✅ Analyse des données
- ✅ Diagramme de classes
- ✅ Modèle physique de données
- ✅ Entités JPA
- ⏳ DAO
- ⏳ Services
- ⏳ Applications