-- ============================================================
--  Hotel Master — Script SQL complet
--  Compatible : MySQL 8.x / MariaDB (XAMPP)
--  Encodage   : UTF-8
-- ============================================================

CREATE DATABASE IF NOT EXISTS hotel_master_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hotel_master_db;

-- ── Désactiver temporairement les FK pour réinitialiser ────────────────────
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS reservation_service_extra;
DROP TABLE IF EXISTS details_chambre;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS chambre;
DROP TABLE IF EXISTS service_extra;
DROP TABLE IF EXISTS hotel;

SET FOREIGN_KEY_CHECKS = 1;

-- ── 1. HOTEL ───────────────────────────────────────────────────────────────
CREATE TABLE hotel (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom       VARCHAR(100)  NOT NULL,
    adresse   VARCHAR(200)  NOT NULL,
    ville     VARCHAR(100)  NOT NULL,
    pays      VARCHAR(100)  NOT NULL,
    etoiles   INT           NOT NULL,
    telephone VARCHAR(30),
    email     VARCHAR(100),
    photo     VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 2. CHAMBRE ─────────────────────────────────────────────────────────────
CREATE TABLE chambre (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero          VARCHAR(20)    NOT NULL,
    type            VARCHAR(50)    NOT NULL,
    prix_par_nuit   DOUBLE         NOT NULL,
    disponible      TINYINT(1)     DEFAULT 1,
    photo           VARCHAR(255),
    hotel_id        BIGINT,
    CONSTRAINT fk_chambre_hotel FOREIGN KEY (hotel_id) REFERENCES hotel(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 3. DETAILS_CHAMBRE (OneToOne → owning side) ────────────────────────────
CREATE TABLE details_chambre (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    superficie              DOUBLE,
    climatisation           TINYINT(1) DEFAULT 0,
    balcon                  TINYINT(1) DEFAULT 0,
    vue_mer                 TINYINT(1) DEFAULT 0,
    wifi_inclus             TINYINT(1) DEFAULT 0,
    nombre_lits             INT,
    type_vue                VARCHAR(50),
    description_equipements TEXT,
    chambre_id              BIGINT UNIQUE,
    CONSTRAINT fk_details_chambre FOREIGN KEY (chambre_id) REFERENCES chambre(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 4. SERVICE_EXTRA ───────────────────────────────────────────────────────
CREATE TABLE service_extra (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100)   NOT NULL,
    prix        DOUBLE         NOT NULL,
    description TEXT,
    icone       VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 5. RESERVATION ─────────────────────────────────────────────────────────
CREATE TABLE reservation (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_client      VARCHAR(100)  NOT NULL,
    prenom_client   VARCHAR(100)  NOT NULL,
    email_client    VARCHAR(150)  NOT NULL,
    date_arrivee    DATE          NOT NULL,
    date_depart     DATE          NOT NULL,
    statut          VARCHAR(30)   DEFAULT 'EN_ATTENTE',
    montant_total   DOUBLE,
    chambre_id      BIGINT,
    CONSTRAINT fk_reservation_chambre FOREIGN KEY (chambre_id) REFERENCES chambre(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 6. TABLE DE JOINTURE N:N reservation ↔ service_extra ─────────────────
CREATE TABLE reservation_service_extra (
    reservation_id    BIGINT NOT NULL,
    service_extra_id  BIGINT NOT NULL,
    PRIMARY KEY (reservation_id, service_extra_id),
    CONSTRAINT fk_rse_reservation  FOREIGN KEY (reservation_id)   REFERENCES reservation(id)   ON DELETE CASCADE,
    CONSTRAINT fk_rse_service_extra FOREIGN KEY (service_extra_id) REFERENCES service_extra(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
--  DONNÉES DE TEST
-- ============================================================

-- Hôtels
INSERT INTO hotel (nom, adresse, ville, pays, etoiles, telephone, email) VALUES
('Grand Hôtel Azur',       '12 Boulevard de la Mer',   'Tunis',       'Tunisie', 5, '+216 70 100 100', 'contact@azur.tn'),
('Hôtel Méditerranée',     '45 Rue des Oliviers',      'Sousse',      'Tunisie', 4, '+216 73 200 200', 'info@mediteranee.tn'),
('Résidence Palm Beach',   '8 Avenue Habib Bourguiba', 'Hammamet',    'Tunisie', 4, '+216 72 300 300', 'palms@beach.tn'),
('Atlas Luxury Hotel',     '3 Place de l\'Indépendance','Sfax',        'Tunisie', 3, '+216 74 400 400', 'atlas@luxury.tn');

-- Chambres
INSERT INTO chambre (numero, type, prix_par_nuit, disponible, hotel_id) VALUES
('101', 'Simple',   80.00,  1, 1),
('102', 'Double',  120.00,  1, 1),
('201', 'Suite',   250.00,  0, 1),
('301', 'Familiale',160.00, 1, 1),
('101', 'Simple',   70.00,  1, 2),
('102', 'Double',  110.00,  0, 2),
('201', 'Deluxe',  200.00,  1, 2),
('101', 'Double',  100.00,  1, 3),
('201', 'Suite',   220.00,  1, 3),
('101', 'Simple',   60.00,  1, 4);

-- Détails Chambres (OneToOne)
INSERT INTO details_chambre (superficie, climatisation, balcon, vue_mer, wifi_inclus, nombre_lits, type_vue, description_equipements, chambre_id) VALUES
(20.0, 1, 0, 0, 1, 1, 'Jardin',  'TV, Minibar, Sèche-cheveux',            1),
(30.0, 1, 1, 1, 1, 2, 'Mer',     'TV 4K, Minibar, Jacuzzi, Balcon',       2),
(55.0, 1, 1, 1, 1, 2, 'Mer',     'Salon, TV 4K, Jacuzzi, Bar privé',      3),
(45.0, 1, 1, 0, 1, 3, 'Jardin',  'Cuisine, 2 TV, Bain bébé',              4),
(18.0, 1, 0, 0, 1, 1, 'Ville',   'TV, Bureau',                            5),
(28.0, 1, 0, 0, 1, 2, 'Ville',   'TV, Minibar',                           6),
(40.0, 1, 1, 1, 1, 2, 'Mer',     'TV 4K, Jacuzzi, Balcon panoramique',    7);

-- Services Extra
INSERT INTO service_extra (nom, prix, description, icone) VALUES
('Petit-déjeuner Buffet',  15.00, 'Buffet varié servi de 7h à 10h',           'fa-coffee'),
('Spa & Bien-être',        50.00, 'Accès illimité au spa et hammam',           'fa-spa'),
('Transfert Aéroport',     35.00, 'Navette aller-retour aéroport',             'fa-car'),
('Dîner Gastronomique',    65.00, 'Repas 3 services avec accord mets/vins',    'fa-utensils'),
('Location Vélo',          12.00, 'Vélo disponible toute la journée',          'fa-bicycle'),
('Baby-sitting',           20.00, 'Service de garde d\'enfants (3h)',          'fa-child'),
('Excursion Desert',       80.00, 'Excursion guidée dans le désert',           'fa-sun');

-- Réservations
INSERT INTO reservation (nom_client, prenom_client, email_client, date_arrivee, date_depart, statut, montant_total, chambre_id) VALUES
('Ben Ali',    'Sami',    'sami.benali@email.com',   '2025-07-10', '2025-07-15', 'CONFIRMÉE',  600.00,  1),
('Trabelsi',   'Leila',   'leila.t@email.com',       '2025-07-20', '2025-07-25', 'CONFIRMÉE',  700.00,  2),
('Chaabane',   'Mohamed', 'med.chaabane@email.com',  '2025-08-01', '2025-08-05', 'EN_ATTENTE', 480.00,  5),
('Dupont',     'Claire',  'claire.d@email.com',      '2025-08-10', '2025-08-17', 'CONFIRMÉE', 1400.00,  3),
('Riahi',      'Anis',    'anis.riahi@email.com',    '2025-09-05', '2025-09-08', 'ANNULÉE',   300.00,  6),
('Karray',     'Ines',    'ines.karray@email.com',   '2025-09-15', '2025-09-20', 'EN_ATTENTE', 550.00,  8);

-- Associations N:N : réservation ↔ services_extra
INSERT INTO reservation_service_extra (reservation_id, service_extra_id) VALUES
(1, 1), (1, 2),        -- Sami : petit-dej + spa
(2, 1), (2, 4),        -- Leila : petit-dej + dîner gastronomique
(3, 1),                -- Mohamed : petit-dej
(4, 1), (4, 2), (4, 3),(4, 4), -- Claire : tout inclus
(6, 1), (6, 5);        -- Ines : petit-dej + vélo

-- ============================================================
--  TABLE app_user (Spring Security JPA — Méthode 2)
--  Créée automatiquement par Hibernate (ddl-auto=update)
--  mais voici la définition manuelle pour référence :
-- ============================================================

CREATE TABLE IF NOT EXISTS app_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(30)  NOT NULL DEFAULT 'ROLE_RECEPTIONIST',
    actif       TINYINT(1)   NOT NULL DEFAULT 1,
    nom_complet VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ⚠️ Les comptes sont créés automatiquement au démarrage via DataInitializer.java
-- admin / admin123  →  ROLE_ADMIN
-- receptionist / recep123  →  ROLE_RECEPTIONIST
-- Vous pouvez aussi en créer via l'interface : http://localhost:8080/admin/users
