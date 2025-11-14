\c postgres;
DROP DATABASE IF EXISTS etantara;
CREATE DATABASE etantara;
\c etantara;
-- ✅ Table "utilisateur" mise à jour pour correspondre à ton entité Java

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    username VARCHAR(100) UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    points INTEGER DEFAULT 0,
    role VARCHAR(50) DEFAULT 'USER',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ============================
-- TABLE : categorie
-- ============================
CREATE TABLE categorie (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description TEXT
);

-- ============================
-- TABLE : contenu_culturel
-- ============================
CREATE TABLE contenu_culturel (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    resume TEXT,
    fichier_contenu VARCHAR(255) NOT NULL,
    fichier_audio VARCHAR(255),
    region VARCHAR(100),
    niveau_difficulte VARCHAR(50),

    categorie_id BIGINT NOT NULL,
    auteur VARCHAR(100),
    est_valide BOOLEAN DEFAULT TRUE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_contenu_categorie
        FOREIGN KEY (categorie_id)
        REFERENCES categorie(id)
        ON DELETE CASCADE
);

-- ============================
-- TABLE : telechargement
-- ============================
CREATE TABLE telechargement (
    id SERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    contenu_id BIGINT,
    date_telechargement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tele_user FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id) ON DELETE SET NULL,

    CONSTRAINT fk_tele_contenu FOREIGN KEY (contenu_id)
        REFERENCES contenu_culturel(id) ON DELETE CASCADE
);

-- ============================
-- TABLE : ecoute
-- ============================
CREATE TABLE ecoute (
    id SERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    contenu_id BIGINT,
    date_ecoute TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ecoute_user FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id) ON DELETE SET NULL,

    CONSTRAINT fk_ecoute_contenu FOREIGN KEY (contenu_id)
        REFERENCES contenu_culturel(id) ON DELETE CASCADE
);

-- ============================
-- TABLE : histoire_partagee
-- ============================
CREATE TABLE histoire_partagee (
    id SERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    titre VARCHAR(255),
    contenu TEXT,
    region VARCHAR(100),
    fichier_audio VARCHAR(255),
    date_partage TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    est_valide BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_hist_user FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id) ON DELETE SET NULL
);

-- ============================
-- TABLE : quiz
-- ============================
CREATE TABLE quiz (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255),
    type_quiz VARCHAR(50),
    difficulte VARCHAR(50),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================
-- TABLE : question
-- ============================
CREATE TABLE question (
    id SERIAL PRIMARY KEY,
    quiz_id BIGINT,
    enonce TEXT NOT NULL,
    reponse_correcte VARCHAR(255) NOT NULL,

    CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id)
        REFERENCES quiz(id)
        ON DELETE CASCADE
);

-- ============================
-- TABLE : reponse_possibles
-- ============================
CREATE TABLE reponse_possibles (
    id SERIAL PRIMARY KEY,
    question_id BIGINT,
    texte VARCHAR(255),

    CONSTRAINT fk_reponse_question
        FOREIGN KEY (question_id)
        REFERENCES question(id)
        ON DELETE CASCADE
);

-- ============================
-- TABLE : score_utilisateur
-- ============================
CREATE TABLE score_utilisateur (
    id SERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    quiz_id BIGINT,
    score INTEGER,
    date_participation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_score_user
        FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_score_quiz
        FOREIGN KEY (quiz_id)
        REFERENCES quiz(id)
        ON DELETE CASCADE
);

-- ============================
-- TABLE : calendrier_culturel
-- ============================
CREATE TABLE calendrier_culturel (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT,
    date_evenement DATE NOT NULL,
    region VARCHAR(100),
    type_evenement VARCHAR(100)
);

-- Script pour créer la table quiz_participation
-- Exécuter ce script dans PostgreSQL

CREATE TABLE IF NOT EXISTS quiz_participation (
    id SERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    date_quiz DATE NOT NULL,
    quiz_id INTEGER NOT NULL,
    reponse_donnee VARCHAR(500),
    est_correct BOOLEAN NOT NULL DEFAULT FALSE,
    points_obtenus INTEGER NOT NULL DEFAULT 0,
    date_participation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Contrainte de clé étrangère
    CONSTRAINT fk_quiz_participation_utilisateur 
        FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) 
        ON DELETE CASCADE,
    
    -- Contrainte unique pour éviter plusieurs participations le même jour
    CONSTRAINT uk_quiz_participation_user_date 
        UNIQUE (utilisateur_id, date_quiz)
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_quiz_participation_user_id ON quiz_participation(utilisateur_id);
CREATE INDEX IF NOT EXISTS idx_quiz_participation_date_quiz ON quiz_participation(date_quiz);
CREATE INDEX IF NOT EXISTS idx_quiz_participation_user_date ON quiz_participation(utilisateur_id, date_quiz);

-- Commentaires
COMMENT ON TABLE quiz_participation IS 'Table pour suivre les participations aux quiz journaliers';
COMMENT ON COLUMN quiz_participation.utilisateur_id IS 'ID de l utilisateur qui a participé';
COMMENT ON COLUMN quiz_participation.date_quiz IS 'Date du quiz (pour identifier le quiz du jour)';
COMMENT ON COLUMN quiz_participation.quiz_id IS 'ID du quiz dans la liste des quiz';
COMMENT ON COLUMN quiz_participation.reponse_donnee IS 'Réponse donnée par l utilisateur';
COMMENT ON COLUMN quiz_participation.est_correct IS 'Si la réponse était correcte';
COMMENT ON COLUMN quiz_participation.points_obtenus IS 'Points gagnés pour cette participation';
COMMENT ON COLUMN quiz_participation.date_participation IS 'Date et heure exacte de la participation';
