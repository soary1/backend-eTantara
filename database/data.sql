-- Insertion des catégories
INSERT INTO categorie (nom, description) VALUES
('Tantara', 'Histoires traditionnelles malgaches transmises de génération en génération'),
('Kabary', 'Discours et allocutions traditionnelles dans les cérémonies malgaches'),
('Ohabolana', 'Proverbes et sagesses malgaches, reflets de la philosophie du peuple'),
('Lovantsofina', 'Contes et leçons transmis oralement, enseignements traditionnels'),
('Saina Gasy', 'Réflexions et pensées malgaches sur la vie et la société');

-- Insertion de contenus culturels de test
INSERT INTO contenu_culturel (titre, resume, fichier_contenu, region, niveau_difficulte, categorie_id, auteur, fichier_audio) VALUES
('Ibonia', 'L''histoire d''Ibonia, le héros légendaire malgache qui combattait le mal et protégeait les innocents', 'contenus/tantara/ibonia.txt', 'Imerina', 'Moyen', 1, 'Tradition orale', NULL),
('Ny Omby sy ny Vorona', 'Tantara momba ny omby iray sy ny vorona iray izay nifankaresy', 'contenus/tantara/omby_vorona.txt', 'Betsileo', 'Facile', 1, 'Tradition orale', NULL),
('Kabary ny Fandrosoana', 'Kabary momba ny fandrosoana sy ny fiainana tsara', 'contenus/kabary/fandrosoana.txt', 'Antandroy', 'Difficile', 2, 'Mpikabary mahay', NULL),
('Izay tsy misy foto-javatra tsy mba maniry', 'Ohabolana milaza fa tsy misy zavatra tsy misy antony', 'contenus/ohabolana/foto_javatra.txt', 'Sakalava', 'Facile', 3, 'Tradition orale', NULL),
('Ny teny toy ny orona', 'Lovantsofina momba ny maha-zava-dehibe ny teny sy ny fitenenana', 'contenus/lovantsofina/teny_orona.txt', 'Bara', 'Moyen', 4, 'Tradition orale', NULL);

-- Insertion d'un utilisateur de test (mot de passe: test123)
INSERT INTO utilisateur (nom, email, mot_de_passe, role) VALUES
('Test User', 'test@etantara.mg', '$2a$10$Nh.5FhIHiSDUKNOaWY9sru4SHdKLYQ7H0q9lgNqp7qkZJJ4zLBH4G', 'USER');
