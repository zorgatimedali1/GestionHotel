# 🏨 Hotel Master — Système de Gestion Hôtelière

> Mini-Projet Spring Boot MVC — 4ème Année Génie Logiciel  
> Enseignant : Saoudi Haythem | Année 2025–2026

---

## 📋 Présentation

**Hotel Master** est une application web complète de gestion hôtelière développée avec Spring Boot 3, Thymeleaf et MySQL. Elle couvre toutes les exigences du cahier des charges :

| Exigence | Implémentation |
|---|---|
| EF1 – CRUD complet | Hôtels, Chambres, Réservations, Services Extra |
| EF2 – Upload d'images | Photos pour Hôtels et Chambres |
| EF3 – Recherche + Pagination | `Pageable` Spring Data sur toutes les listes |
| EF4 – Dashboard | 10 KPIs via `@Query` JPQL (COUNT, SUM, AVG) |
| EF5 – Validation | Jakarta Bean Validation + `BindingResult` + `th:errors` |

---

## 🗄️ Relations JPA Implémentées

```
Hotel (1) ──────────────── (N) Chambre
                                  │
           (1:1) DetailsChambre ──┘
                 (fiche technique)

Reservation (N) ──── (N) ServiceExtra
     │                   (table: reservation_service_extra)
     └── (N:1) Chambre
```

| Relation | Entités | Annotation |
|---|---|---|
| **@OneToMany** | `Hotel` → `Chambre` | `@OneToMany(mappedBy="hotel")` |
| **@OneToOne** | `Chambre` ↔ `DetailsChambre` | `@OneToOne` / `@OneToOne(mappedBy="chambre")` |
| **@ManyToMany** | `Reservation` ↔ `ServiceExtra` | `@ManyToMany` + `@JoinTable` |

---

## 🛠️ Stack Technique

| Composant | Technologie |
|---|---|
| Backend | Spring Boot 3.2.0 |
| Templates | Thymeleaf |
| Base de données | MySQL 8.x (XAMPP port 3306) |
| ORM | Spring Data JPA / Hibernate |
| CSS / UI | Bootstrap 5.3 + FontAwesome 6 |
| Validation | Jakarta Bean Validation |
| Build | Maven |
| Java | 17 |

---

## 🚀 Installation et Lancement

### Prérequis

- Java 17+
- Maven 3.8+
- XAMPP (Apache + MySQL actifs)
- IDE : IntelliJ IDEA (recommandé)

---

### Étape 1 — Importer la base de données

**Via phpMyAdmin (recommandé) :**

1. Démarrer XAMPP → cliquer **Start** sur Apache et MySQL
2. Ouvrir [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
3. Cliquer sur **Importer** dans la barre du haut
4. Choisir le fichier `hotel_master_db.sql` situé à la racine du projet
5. Cliquer **Exécuter**

**Via la ligne de commande MySQL :**

```bash
mysql -u root -p < hotel_master_db.sql
```

> Le script crée automatiquement la base `hotel_master_db` et insère les données de test.

---

### Étape 2 — Vérifier `application.properties`

Le fichier est pré-configuré pour XAMPP (port 3306, sans mot de passe) :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_master_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

> Si vous avez défini un mot de passe MySQL dans XAMPP, renseignez-le dans `spring.datasource.password`.

---

### Étape 3 — Lancer l'application

**Depuis IntelliJ IDEA :**

1. `File → Open` → sélectionner le dossier `HotelMaster`
2. Attendre que Maven télécharge les dépendances
3. Ouvrir `HotelMasterApplication.java`
4. Cliquer sur le bouton ▶ **Run**

**Depuis le terminal (Maven Wrapper) :**

```bash
cd HotelMaster
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows
```

---

### Étape 4 — Accéder à l'application

Ouvrir le navigateur : **[http://localhost:8080](http://localhost:8080)**

Vous serez redirigé automatiquement vers le **Dashboard**.

---

## 🗂️ Structure du Projet

```
HotelMaster/
├── src/main/java/com/hotelmaster/hotelmaster/
│   ├── HotelMasterApplication.java
│   ├── controller/
│   │   ├── DashboardController.java
│   │   ├── HotelController.java
│   │   ├── ChambreController.java
│   │   ├── ReservationController.java
│   │   └── ServiceExtraController.java
│   ├── entities/
│   │   ├── Hotel.java
│   │   ├── Chambre.java
│   │   ├── DetailsChambre.java
│   │   ├── Reservation.java
│   │   └── ServiceExtra.java
│   ├── repository/
│   │   ├── HotelRepository.java
│   │   ├── ChambreRepository.java
│   │   ├── DetailsChambreRepository.java
│   │   ├── ReservationRepository.java
│   │   └── ServiceExtraRepository.java
│   └── service/
│       ├── hotel/         IServiceHotel.java + ServiceHotel.java
│       ├── chambre/       IServiceChambre.java + ServiceChambre.java
│       ├── reservation/   IServiceReservation.java + ServiceReservation.java
│       ├── serviceextra/  IServiceExtra.java + ServiceExtraImpl.java
│       └── dashboard/     ServiceDashboard.java + DashboardStatsDTO.java
├── src/main/resources/
│   ├── application.properties
│   ├── static/uploads/       ← images uploadées
│   └── templates/
│       ├── fragments/layout.html   ← sidebar + layout partagé
│       ├── dashboard.html
│       ├── hotels/       list.html | form.html | detail.html
│       ├── chambres/     list.html | form.html | details.html
│       ├── reservations/ list.html | form.html
│       └── services/     list.html | form.html
├── hotel_master_db.sql    ← Script SQL complet
├── pom.xml
└── README.md
```

---

## 📌 URLs Principales

| Page | URL |
|---|---|
| Dashboard | `http://localhost:8080/` |
| Liste Hôtels | `http://localhost:8080/hotels` |
| Liste Chambres | `http://localhost:8080/chambres` |
| Liste Réservations | `http://localhost:8080/reservations` |
| Services Extra | `http://localhost:8080/services-extra` |

---

## 💡 Points Architecturaux Clés

### Dashboard — Calcul via JPQL (et non Java Streams)
Conformément à la recommandation du cahier des charges, les KPIs du dashboard sont calculés **directement en base de données** via des requêtes `@Query` JPQL (`COUNT`, `SUM`, `AVG`) dans les Repositories. Cela évite le chargement de toutes les pages en mémoire.

### ManyToMany — Sélection multiple
Le formulaire de réservation utilise `<select multiple name="servicesExtraIds">`. Le Controller reçoit la liste des IDs via `@RequestParam List<Long> servicesExtraIds` et charge les entités depuis le repository avant de les attacher à la réservation.

### OneToOne — Owning Side
`DetailsChambre` porte la clé étrangère (`chambre_id`) — c'est le **owning side**. `Chambre` déclare `@OneToOne(mappedBy = "chambre")`.

### Upload d'Images
Les fichiers sont sauvegardés dans `src/main/resources/static/uploads/` avec un nom UUID unique. Ils sont servis statiquement par Spring Boot via `/uploads/{filename}`.

---

## 🐱 Déploiement sur Apache Tomcat 9.0.115

### Prérequis supplémentaires
- **Apache Tomcat 9.0.115** installé et configuré
- **Java 17** (Tomcat 9 + Spring Boot 3 requièrent Java 17+)
- XAMPP démarré (MySQL actif, base importée)

---

### Étape 1 — Compiler et générer le fichier WAR

Depuis la racine du projet (`HotelMaster/`) :

```bash
mvn clean package -DskipTests
```

Le fichier WAR est généré dans :
```
HotelMaster/target/HotelMaster.war
```

> `-DskipTests` évite d'exécuter les tests (qui nécessitent une BDD active).

---

### Étape 2 — Copier le WAR dans Tomcat

Copier `HotelMaster.war` dans le dossier **webapps** de Tomcat :

```
apache-tomcat-9.0.115/
└── webapps/
    └── HotelMaster.war   ← coller ici
```

Tomcat va décompresser automatiquement le WAR au démarrage.

---

### Étape 3 — Démarrer Tomcat

**Windows :**
```bat
apache-tomcat-9.0.115\bin\startup.bat
```

**Linux / macOS :**
```bash
chmod +x apache-tomcat-9.0.115/bin/*.sh
./apache-tomcat-9.0.115/bin/startup.sh
```

---

### Étape 4 — Accéder à l'application

```
http://localhost:8080/HotelMaster
```

> Le chemin `/HotelMaster` correspond au nom du WAR.  
> Vous serez redirigé automatiquement vers la page de connexion.

---

### ⚙️ Configuration Java pour Tomcat (si besoin)

Si Tomcat ne démarre pas avec Java 17, définir `JAVA_HOME` :

**Windows** (dans `bin/setenv.bat`, à créer si absent) :
```bat
set JAVA_HOME=C:\Program Files\Java\jdk-17
set JRE_HOME=%JAVA_HOME%
```

**Linux / macOS** (dans `bin/setenv.sh`) :
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export JRE_HOME=$JAVA_HOME
```

---

### 🔍 Vérifier les logs Tomcat

En cas d'erreur, consulter :
```
apache-tomcat-9.0.115/logs/catalina.out
```

---

### 📌 Résumé des URLs selon le mode de lancement

| Mode | URL d'accès |
|---|---|
| **Tomcat externe** (WAR) | `http://localhost:8080/HotelMaster` |
| **Maven intégré** (`mvn spring-boot:run`) | `http://localhost:8080` |
| **IntelliJ Run** | `http://localhost:8080` |
#   G e s t i o n H o t e l  
 