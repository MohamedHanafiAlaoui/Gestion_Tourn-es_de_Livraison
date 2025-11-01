# Gestion des Tournées de Livraison

Application Spring Boot (Java 17, Maven) pour gérer les tournées de livraison, les véhicules, les entrepôts et l’optimisation d’itinéraires.

## Sommaire

- **Aperçu**
- **Fonctionnalités**
- **Stack technique**
- **Prérequis**
- **Installation & exécution**
- **Documentation API**
- **Scripts Maven utiles**
- **Structure du projet**
- **Endpoints clés (exemple)**
- **Tests**
- **Déploiement**
- **Screenshots**

## Aperçu

Ce service expose une API REST pour:
- **Créer et gérer des tournées** avec affectation optionnelle d’un véhicule et d’un entrepôt.
- **Optimiser l’ordre des livraisons** (nearest neighbor simple et variantes selon le contexte).
- **Calculer la distance totale** d’une tournée avec retour à l’entrepôt quand présent.

## Fonctionnalités

- **CRUD Tournée** (exemples d’API ci-dessous).
- **Optimisation d’itinéraire** en fonction des informations disponibles (entrepôt, type de véhicule).
- **Validation capacité véhicule** (poids/volume) lors de l’affectation de livraisons.
- **OpenAPI/Swagger UI** via springdoc.

## Stack technique

- **Langage**: Java 17
- **Framework**: Spring Boot 3
- **Build**: Maven
- **Packaging**: WAR (Tomcat en scope provided)
- **Persistence**: Spring Data JPA
- **Base de données**: H2 (runtime)
- **Migrations**: Liquibase (dépendance présente)
- **Docs API**: springdoc-openapi-starter-webmvc-ui

## Prérequis

- Java 17+
- Maven 3.9+

## Installation & exécution

1. Cloner le dépôt

```bash
git clone <votre-url-repo>
cd Livraison
```

2. Lancer l’application en développement

```bash
mvn spring-boot:run
```

- Par défaut, l’application écoute sur `http://localhost:8080`.

## Documentation API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

> Note: Les chemins exacts peuvent varier selon la config. Les dépendances springdoc sont présentes dans le `pom.xml`.

## Scripts Maven utiles

- **Build** (génère un WAR):

```bash
mvn clean package
```

- **Tests**:

```bash
mvn test
```

## Structure du projet

```
Livraison/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/Livraison/
│  │  │  ├─ LivraisonApplication.java
│  │  │  ├─ controller/
│  │  │  │  └─ TourController.java
│  │  │  ├─ service/
│  │  │  │  ├─ TourService.java
│  │  │  │  └─ ...
│  │  └─ resources/
│  │     ├─ applicationContext.xml
│  │     └─ ...
│  └─ test/
│     └─ java/com/example/Livraison/
│        └─ LivraisonApplicationTests.java
└─ README.md
```

## Endpoints clés (exemple)

Base path: `/api/tours`

- **GET** `/api/tours`
  - Liste toutes les tournées (`List<TourDTO>`)

- **GET** `/api/tours/{id}`
  - Détail d’une tournée (`TourDTO`)

- **POST** `/api/tours`
  - Crée une tournée
  - Corps: `TourDTO` avec, par exemple, `warehouseId`, `vehiculeId` (optionnel), `deliveryIds` (optionnel)

- **GET** `/api/tours/{id}/optimize`
  - Retourne la liste des livraisons ordonnées (`List<DeliveryDTO>`)

- **GET** `/api/tours/{id}/distance`
  - Calcule la distance totale (double)

> Des erreurs métier peuvent être renvoyées (ex: véhicule non disponible, dépassement de capacité, entrepôt manquant, etc.).

## Tests

Exécuter l’ensemble des tests:

```bash
mvn test
```

## Déploiement

Le packaging est **WAR** avec Tomcat en `provided`. Pour un déploiement classique:

- Déployer le WAR généré (`target/Livraison-0.0.1-SNAPSHOT.war`) sur un conteneur **Tomcat** externe compatible.
- Pour l’exécution locale en dev, privilégier `mvn spring-boot:run`.

## Screenshots

<img width="1909" height="900" alt="image" src="https://github.com/user-attachments/assets/66664bc9-d748-4bbe-87bb-2e9f88955d93" />

<img width="1878" height="852" alt="image" src="https://github.com/user-attachments/assets/d59d28d4-4b2b-4dec-966c-e75a33e91928" />


<img width="1073" height="793" alt="image" src="https://github.com/user-attachments/assets/8af4eb48-f1b0-41da-b6a3-336fe305b297" />



