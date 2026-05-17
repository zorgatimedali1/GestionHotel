# 🏨 Hotel Master — Système de Gestion Hôtelière

> Mini-Projet Spring Boot MVC — 4ème Année Génie Logiciel  
> Enseignant : Saoudi Haythem | Année 2025–2026

---

## 📋 Présentation

**Hotel Master** est une application web complète de gestion hôtelière développée avec Spring Boot 3, Thymeleaf et MySQL.

Elle couvre toutes les exigences du cahier des charges :

| Exigence | Implémentation |
|---|---|
| EF1 – CRUD complet | Hôtels, Chambres, Réservations, Services Extra |
| EF2 – Upload d'images | Photos pour Hôtels et Chambres |
| EF3 – Recherche + Pagination | `Pageable` Spring Data |
| EF4 – Dashboard | KPIs via `@Query` JPQL |
| EF5 – Validation | Jakarta Bean Validation |

---

## 🗄️ Relations JPA Implémentées

```text
Hotel (1) ──────────────── (N) Chambre
                                  │
           (1:1) DetailsChambre ──┘

Reservation (N) ──── (N) ServiceExtra
     │
     └── (N:1) Chambre
```

| Relation | Implémentation |
|---|---|
| `Hotel` → `Chambre` | `@OneToMany(mappedBy="hotel")` |
| `Chambre` ↔ `DetailsChambre` | `@OneToOne` |
| `Reservation` ↔ `ServiceExtra` | `@ManyToMany + @JoinTable` |

---

## 🛠️ Stack Technique

| Composant | Technologie |
|---|---|
| Backend | Spring Boot 3.2 |
| Templates | Thymeleaf |
| Base de données | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| UI | Bootstrap 5.3 + FontAwesome |
| Validation | Jakarta Validation |
| Build | Maven |
| Java | 17 |

---

# 🚀 Installation et Lancement

## ✅ Prérequis

- Java 17+
- Maven 3.8+
- XAMPP (Apache + MySQL)
- IntelliJ IDEA

---

## 📥 Étape 1 — Importer la base de données

### Via phpMyAdmin

1. Démarrer XAMPP
2. Ouvrir :

```text
http://localhost/phpmyadmin
```

3. Importer :

```text
hotel_master_db.sql
```

---

## ⚙️ Étape 2 — Vérifier `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_master_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

---

## ▶️ Étape 3 — Lancer l'application

### IntelliJ IDEA

- Ouvrir le projet
- Exécuter :

```text
HotelMasterApplication.java
```

### Terminal

```bash
cd HotelMaster

# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

---

## 🌐 Étape 4 — Accéder à l'application

```text
http://localhost:8080
```

---

# 🗂️ Structure du Projet

```text
HotelMaster/
├── src/main/java/com/hotelmaster/hotelmaster/
│   ├── controller/
│   ├── entities/
│   ├── repository/
│   └── service/
│
├── src/main/resources/
│   ├── static/uploads/
│   └── templates/
│
├── hotel_master_db.sql
├── pom.xml
└── README.md
```

---

# 📌 URLs Principales

| Page | URL |
|---|---|
| Dashboard | `http://localhost:8080/` |
| Hôtels | `http://localhost:8080/hotels` |
| Chambres | `http://localhost:8080/chambres` |
| Réservations | `http://localhost:8080/reservations` |
| Services Extra | `http://localhost:8080/services-extra` |

---

# 💡 Points Architecturaux

## Dashboard — JPQL

Les KPIs sont calculés directement via :

```java
@Query
```

avec `COUNT`, `SUM`, `AVG`.

---

## ManyToMany

```html
<select multiple name="servicesExtraIds">
```

```java
@RequestParam List<Long> servicesExtraIds
```

---

## Upload d'Images

Les images sont stockées dans :

```text
src/main/resources/static/uploads/
```

avec un nom UUID unique.

---

# 🐱 Déploiement Tomcat 9

## Générer le WAR

```bash
mvn clean package -DskipTests
```

WAR généré :

```text
target/HotelMaster.war
```

---

## Copier dans Tomcat

```text
apache-tomcat-9/webapps/HotelMaster.war
```

---

## Démarrer Tomcat

### Windows

```bat
startup.bat
```

### Linux / macOS

```bash
./startup.sh
```

---

## URL Tomcat

```text
http://localhost:8080/HotelMaster
```

---

# 📌 Résumé des URLs

| Mode | URL |
|---|---|
| Spring Boot | `http://localhost:8080` |
| Tomcat WAR | `http://localhost:8080/HotelMaster` |

---

# 👨‍💻 Auteur

Projet réalisé dans le cadre du module :

**Spring Boot MVC — Génie Logiciel 4ème année**
