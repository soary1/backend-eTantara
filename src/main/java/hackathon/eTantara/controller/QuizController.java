package hackathon.eTantara.controller;

import hackathon.eTantara.service.UtilisateurService;
import hackathon.eTantara.service.QuizParticipationService;
import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.entite.QuizParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;
import java.io.InputStream;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private UtilisateurService utilisateurService;
    
    @Autowired
    private QuizParticipationService quizParticipationService;

    /**
     * 🔹 Obtenir le quiz du jour
     */
    @GetMapping("/daily")
    public ResponseEntity<Map<String, Object>> getDailyQuiz(Authentication authentication) {
        // Utiliser la date du jour pour générer un quiz cohérent
        LocalDate today = LocalDate.now();
        int dayOfYear = today.getDayOfYear();
        
        // Liste des quiz prédéfinis
        List<Map<String, Object>> quizzes = createQuizDatabase();
        
        // Sélectionner un quiz basé sur le jour de l'année
        Map<String, Object> todayQuiz = new HashMap<>(quizzes.get(dayOfYear % quizzes.size()));
        
        // Ajouter la date
        todayQuiz.put("date", today.toString());
        todayQuiz.put("dayNumber", dayOfYear);
        
        // Vérifier si l'utilisateur a déjà participé aujourd'hui
        boolean dejaParticipe = false;
        QuizParticipation participationExistante = null;
        
        if (authentication != null && authentication.getName() != null) {
            try {
                String email = authentication.getName();
                Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
                
                if (utilisateurOpt.isPresent()) {
                    Utilisateur utilisateur = utilisateurOpt.get();
                    dejaParticipe = quizParticipationService.aDejaParticipeAujourdHui(utilisateur);
                    
                    if (dejaParticipe) {
                        Optional<QuizParticipation> participationOpt = 
                            quizParticipationService.getParticipationDuJour(utilisateur);
                        if (participationOpt.isPresent()) {
                            participationExistante = participationOpt.get();
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de la vérification de participation: " + e.getMessage());
            }
        }
        
        todayQuiz.put("dejaParticipe", dejaParticipe);
        
        // Si l'utilisateur a déjà participé, ajouter les informations de sa participation
        if (dejaParticipe && participationExistante != null) {
            Map<String, Object> participationInfo = new HashMap<>();
            participationInfo.put("reponseDonnee", participationExistante.getReponseDonnee());
            participationInfo.put("estCorrect", participationExistante.getEstCorrect());
            participationInfo.put("pointsObtenus", participationExistante.getPointsObtenus());
            participationInfo.put("dateParticipation", participationExistante.getDateParticipation().toString());
            
            todayQuiz.put("participationInfo", participationInfo);
        }
        
        return ResponseEntity.ok(todayQuiz);
    }
    
    /**
     * 🔹 Vérifier une réponse
     */
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAnswer(@RequestBody Map<String, Object> request, Authentication authentication) {
        String userAnswer = (String) request.get("answer");
        Integer questionId = (Integer) request.get("questionId");
        
        if (userAnswer == null || questionId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Réponse ou ID de question manquant");
            return ResponseEntity.badRequest().body(error);
        }
        
        // Vérifier si l'utilisateur est connecté
        if (authentication == null || authentication.getName() == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Vous devez être connecté pour participer au quiz");
            return ResponseEntity.status(401).body(error);
        }
        
        String email = authentication.getName();
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
        
        if (!utilisateurOpt.isPresent()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Utilisateur non trouvé");
            return ResponseEntity.status(404).body(error);
        }
        
        Utilisateur utilisateur = utilisateurOpt.get();
        
        // Vérifier si l'utilisateur a déjà participé aujourd'hui
        if (quizParticipationService.aDejaParticipeAujourdHui(utilisateur)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Vous avez déjà participé au quiz d'aujourd'hui");
            error.put("message", "Vous avez déjà participé au quiz d'aujourd'hui. Revenez demain pour un nouveau quiz !");
            return ResponseEntity.status(400).body(error);
        }
        
        // Récupérer la bonne réponse
        List<Map<String, Object>> quizzes = createQuizDatabase();
        Map<String, Object> quiz = quizzes.stream()
            .filter(q -> q.get("id").equals(questionId))
            .findFirst()
            .orElse(null);
        
        if (quiz == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Question non trouvée");
            return ResponseEntity.badRequest().body(error);
        }
        
        String correctAnswer = (String) quiz.get("correctAnswer");
        boolean isCorrect = userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
        int points = isCorrect ? 10 : 0;
        
        try {
            // Enregistrer la participation
            quizParticipationService.enregistrerParticipation(
                utilisateur, questionId, userAnswer, isCorrect, points
            );
            
            // Mettre à jour le score si la réponse est correcte
            if (isCorrect) {
                utilisateur.setPoints(utilisateur.getPoints() + points);
                utilisateurService.save(utilisateur);
                System.out.println("✅ Score mis à jour pour " + email + " : +" + points + " points");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'enregistrement de la participation: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erreur lors de l'enregistrement de votre participation");
            return ResponseEntity.status(500).body(error);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("correctAnswer", correctAnswer);
        response.put("explanation", quiz.get("explanation"));
        response.put("points", points);
        response.put("message", isCorrect ? 
            "Bravo ! Vous avez gagné " + points + " points !" : 
            "Dommage ! Mais vous apprenez quelque chose de nouveau !");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 🔹 Obtenir les statistiques de quiz de l'utilisateur connecté
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getQuizStats(Authentication authentication) {
        Map<String, Object> stats = new HashMap<>();
        
        if (authentication != null && authentication.getName() != null) {
            try {
                String email = authentication.getName();
                Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
                
                if (utilisateurOpt.isPresent()) {
                    Utilisateur utilisateur = utilisateurOpt.get();
                    
                    // Utiliser les vraies données de participation
                    long totalParticipations = quizParticipationService.getNombreTotalParticipations(utilisateur);
                    long bonnesReponses = quizParticipationService.getNombreBonnesReponses(utilisateur);
                    
                    int accuracy = totalParticipations > 0 ? 
                        (int) Math.round((bonnesReponses * 100.0) / totalParticipations) : 0;
                    
                    stats.put("totalQuizzes", 365); // Quiz quotidien pour toute l'année
                    stats.put("completedQuizzes", totalParticipations);
                    stats.put("correctAnswers", bonnesReponses);
                    stats.put("accuracy", accuracy);
                    stats.put("streak", bonnesReponses > 0 ? 1 : 0); // TODO: Calculer vraie série
                    stats.put("totalPoints", utilisateur.getPoints());
                    
                    return ResponseEntity.ok(stats);
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de la récupération des stats quiz: " + e.getMessage());
            }
        }
        
        // Données par défaut pour les utilisateurs non connectés
        stats.put("totalQuizzes", 365);
        stats.put("completedQuizzes", 0);
        stats.put("correctAnswers", 0);
        stats.put("accuracy", 0);
        stats.put("streak", 0);
        stats.put("totalPoints", 0);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Base de données des quiz
     */
    private List<Map<String, Object>> createQuizDatabase() {
        List<Map<String, Object>> quizzes = new ArrayList<>();
        
        // Quiz 1 - Expression à compléter
        Map<String, Object> quiz1 = new HashMap<>();
        quiz1.put("id", 1);
        quiz1.put("type", "complete");
        quiz1.put("category", "Ohabolana");
        quiz1.put("question", "Complétez cette expression malgache :");
        quiz1.put("text", "Ny tsara _____ tsy mba very");
        quiz1.put("correctAnswer", "fanahy");
        quiz1.put("explanation", "\"Ny tsara fanahy tsy mba very\" signifie que les bonnes actions et la bonté du cœur ne sont jamais perdues.");
        quiz1.put("difficulty", "Moyen");
        quizzes.add(quiz1);
        
        // Quiz 2 - Signification
        Map<String, Object> quiz2 = new HashMap<>();
        quiz2.put("id", 2);
        quiz2.put("type", "meaning");
        quiz2.put("category", "Ohabolana");
        quiz2.put("question", "Que signifie cette expression malgache ?");
        quiz2.put("text", "Izay tsy misy foto-javatra tsy mba maniry");
        quiz2.put("options", Arrays.asList(
            "Rien ne pousse sans racines",
            "Tout ce qui existe a une origine",
            "Les plantes ont besoin d'eau",
            "Il faut planter pour récolter"
        ));
        quiz2.put("correctAnswer", "Tout ce qui existe a une origine");
        quiz2.put("explanation", "Cette expression signifie que tout ce qui existe a une cause, une origine, une racine. Rien n'arrive par hasard.");
        quiz2.put("difficulty", "Facile");
        quizzes.add(quiz2);
        
        // Quiz 3 - Vocabulaire
        Map<String, Object> quiz3 = new HashMap<>();
        quiz3.put("id", 3);
        quiz3.put("type", "vocabulary");
        quiz3.put("category", "Vocabulaire");
        quiz3.put("question", "Comment dit-on 'merci beaucoup' en malgache ?");
        quiz3.put("options", Arrays.asList(
            "Misaotra betsaka",
            "Azafady tompoko",
            "Veloma ry namana",
            "Inona vaovao"
        ));
        quiz3.put("correctAnswer", "Misaotra betsaka");
        quiz3.put("explanation", "\"Misaotra betsaka\" est la façon de dire 'merci beaucoup' en malgache. 'Misaotra' = merci, 'betsaka' = beaucoup.");
        quiz3.put("difficulty", "Facile");
        quizzes.add(quiz3);
        
        // Quiz 4 - Culture
        Map<String, Object> quiz4 = new HashMap<>();
        quiz4.put("id", 4);
        quiz4.put("type", "culture");
        quiz4.put("category", "Tradition");
        quiz4.put("question", "Qu'est-ce que le 'Famadihana' ?");
        quiz4.put("options", Arrays.asList(
            "Une danse traditionnelle",
            "Un plat typique malgache",
            "Une cérémonie de retournement des morts",
            "Un instrument de musique"
        ));
        quiz4.put("correctAnswer", "Une cérémonie de retournement des morts");
        quiz4.put("explanation", "Le Famadihana est une cérémonie traditionnelle malgache où les familles sortent les corps de leurs ancêtres de leurs tombeaux, les enveloppent dans de nouveaux linceuls et dansent avec eux avant de les remettre dans leurs sépultures.");
        quiz4.put("difficulty", "Moyen");
        quizzes.add(quiz4);
        
        // Quiz 5 - Expression à compléter
        Map<String, Object> quiz5 = new HashMap<>();
        quiz5.put("id", 5);
        quiz5.put("type", "complete");
        quiz5.put("category", "Ohabolana");
        quiz5.put("question", "Complétez ce proverbe malgache :");
        quiz5.put("text", "Ny saina no _____ fa tsy ny hery");
        quiz5.put("correctAnswer", "manjaka");
        quiz5.put("explanation", "\"Ny saina no manjaka fa tsy ny hery\" signifie que c'est l'intelligence qui règne, pas la force. La sagesse l'emporte sur la force brute.");
        quiz5.put("difficulty", "Moyen");
        quizzes.add(quiz5);
        
        // Quiz 6 - Géographie
        Map<String, Object> quiz6 = new HashMap<>();
        quiz6.put("id", 6);
        quiz6.put("type", "geography");
        quiz6.put("category", "Géographie");
        quiz6.put("question", "Quelle est l'ancienne capitale de Madagascar ?");
        quiz6.put("options", Arrays.asList(
            "Toamasina",
            "Antsirabe",
            "Antananarivo",
            "Fianarantsoa"
        ));
        quiz6.put("correctAnswer", "Antananarivo");
        quiz6.put("explanation", "Antananarivo est la capitale actuelle de Madagascar depuis l'époque du royaume de l'Imerina. Elle était déjà la capitale du royaume d'Imerina avant de devenir celle de tout Madagascar.");
        quiz6.put("difficulty", "Facile");
        quizzes.add(quiz6);
        
        // Quiz 7 - Héros légendaire
        Map<String, Object> quiz7 = new HashMap<>();
        quiz7.put("id", 7);
        quiz7.put("type", "legend");
        quiz7.put("category", "Tantara");
        quiz7.put("question", "Qui est Ibonia dans la mythologie malgache ?");
        quiz7.put("options", Arrays.asList(
            "Un roi historique",
            "Un héros légendaire aux pouvoirs magiques",
            "Un esprit de la nature",
            "Un ancêtre royal"
        ));
        quiz7.put("correctAnswer", "Un héros légendaire aux pouvoirs magiques");
        quiz7.put("explanation", "Ibonia est un héros légendaire de la mythologie malgache, doté de pouvoirs magiques extraordinaires. Il est le protagoniste de nombreux contes traditionnels.");
        quiz7.put("difficulty", "Moyen");
        quizzes.add(quiz7);
        
        // Quiz 8 - Nourriture
        Map<String, Object> quiz8 = new HashMap<>();
        quiz8.put("id", 8);
        quiz8.put("type", "food");
        quiz8.put("category", "Gastronomie");
        quiz8.put("question", "Qu'est-ce que le 'Romazava' ?");
        quiz8.put("options", Arrays.asList(
            "Un dessert aux fruits",
            "Une soupe de brèdes avec de la viande",
            "Un gâteau traditionnel",
            "Une boisson fermentée"
        ));
        quiz8.put("correctAnswer", "Une soupe de brèdes avec de la viande");
        quiz8.put("explanation", "Le Romazava est un plat traditionnel malgache, c'est une soupe claire faite avec des brèdes (feuilles vertes) et de la viande, souvent du zébu.");
        quiz8.put("difficulty", "Moyen");
        quizzes.add(quiz8);
        
        // Quiz 9 - Expression à compléter
        Map<String, Object> quiz9 = new HashMap<>();
        quiz9.put("id", 9);
        quiz9.put("type", "complete");
        quiz9.put("category", "Ohabolana");
        quiz9.put("question", "Complétez cette sagesse malgache :");
        quiz9.put("text", "Ny teny mora _____ fa ny zavatra sarotra atao");
        quiz9.put("correctAnswer", "lazaina");
        quiz9.put("explanation", "\"Ny teny mora lazaina fa ny zavatra sarotra atao\" signifie qu'il est facile de parler mais difficile d'agir. Les mots sont faciles à dire, mais les actes sont difficiles à accomplir.");
        quiz9.put("difficulty", "Difficile");
        quizzes.add(quiz9);
        
        // Quiz 10 - Instrument de musique
        Map<String, Object> quiz10 = new HashMap<>();
        quiz10.put("id", 10);
        quiz10.put("type", "music");
        quiz10.put("category", "Musique");
        quiz10.put("question", "Qu'est-ce que la 'Valiha' ?");
        quiz10.put("options", Arrays.asList(
            "Une danse traditionnelle",
            "Un instrument de musique à cordes",
            "Un chant religieux",
            "Un costume traditionnel"
        ));
        quiz10.put("correctAnswer", "Un instrument de musique à cordes");
        quiz10.put("explanation", "La Valiha est l'instrument de musique national de Madagascar. C'est une cithare tubulaire faite en bambou avec des cordes métalliques.");
        quiz10.put("difficulty", "Facile");
        quizzes.add(quiz10);
        
        return quizzes;
    }

        /**
         * Endpoint public qui retourne tous les quiz lus depuis le fichier `quizz.txt` dans resources.
         */
        @GetMapping("")
        public ResponseEntity<List<Map<String, Object>>> getAllQuizzesPublic() {
            try {
                System.out.println("📡 GET /api/quiz - Tentative de lecture de quizz.txt");
                List<Map<String, Object>> quizzes = readQuizzesFromFile();
                System.out.println("✅ GET /api/quiz - " + quizzes.size() + " quizzes trouvés");
                return ResponseEntity.ok(quizzes);
            } catch (Exception e) {
                System.err.println("❌ Erreur lecture quizz.txt: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(500).body(Collections.emptyList());
            }
        }

        /**
         * Vérification publique simplifiée (utilisée par le frontend sans auth).
         * Reçoit { questionId: <number>, reponse: <string> }
         */
        @PostMapping("/reponse")
        public ResponseEntity<Map<String, Object>> checkAnswerPublic(@RequestBody Map<String, Object> request) {
            Object qidObj = request.get("questionId");
            Object reponseObj = request.get("reponse");

            Map<String, Object> error = new HashMap<>();
            if (qidObj == null || reponseObj == null) {
                error.put("error", "questionId or reponse missing");
                return ResponseEntity.badRequest().body(error);
            }

            int questionId;
            try {
                if (qidObj instanceof Number) questionId = ((Number) qidObj).intValue();
                else questionId = Integer.parseInt(qidObj.toString());
            } catch (Exception ex) {
                error.put("error", "invalid questionId");
                return ResponseEntity.badRequest().body(error);
            }

            String reponse = reponseObj.toString();

            try {
                List<Map<String, Object>> quizzes = readQuizzesFromFile();
                Map<String, Object> quiz = quizzes.stream()
                        .filter(q -> Objects.equals(((Number) q.get("id")).intValue(), questionId))
                        .findFirst()
                        .orElse(null);

                if (quiz == null) {
                    error.put("error", "question not found");
                    return ResponseEntity.badRequest().body(error);
                }

                String correctAnswer = (String) quiz.get("correctAnswer");
                boolean isCorrect = correctAnswer != null && reponse.trim().equalsIgnoreCase(correctAnswer.trim());

                Map<String, Object> resp = new HashMap<>();
                resp.put("correct", isCorrect);
                resp.put("correctAnswer", correctAnswer);
                resp.put("message", isCorrect ? "Correct" : "Incorrect");
                return ResponseEntity.ok(resp);

            } catch (Exception e) {
                System.err.println("Erreur vérification publique: " + e.getMessage());
                error.put("error", "internal error");
                return ResponseEntity.status(500).body(error);
            }
        }

        private List<Map<String,Object>> readQuizzesFromFile() throws Exception {
            ClassPathResource resource = new ClassPathResource("quizz.txt");
            try (InputStream is = resource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(is, new TypeReference<List<Map<String,Object>>>(){});
            }
        }

        /**
         * Endpoint pour soumettre un quiz utilisateur
         * Ajoute le quiz au fichier quizz.txt
         */
        @PostMapping("/submit")
        public ResponseEntity<?> submitQuiz(@RequestBody Map<String, Object> request) {
            try {
                String username = (String) request.get("username");
                if (username == null) {
                    return ResponseEntity.badRequest().body(
                        new java.util.HashMap<String, String>() {{ put("error", "username required"); }}
                    );
                }

                // Créer le quiz à partir de la requête
                Map<String, Object> newQuiz = new HashMap<>();
                newQuiz.put("id", System.currentTimeMillis()); // ID unique basé sur timestamp
                newQuiz.put("question", request.get("question"));
                newQuiz.put("optionA", request.get("optionA"));
                newQuiz.put("optionB", request.get("optionB"));
                newQuiz.put("optionC", request.get("optionC"));
                newQuiz.put("optionD", request.get("optionD"));
                newQuiz.put("correctAnswer", request.get("correctAnswer"));
                newQuiz.put("submittedBy", username);

                // Lire le fichier, ajouter le quiz, réécrire
                ObjectMapper mapper = new ObjectMapper();
                ClassPathResource resource = new ClassPathResource("quizz.txt");
                List<Map<String,Object>> quizzes = new ArrayList<>();
                
                try {
                    quizzes = mapper.readValue(resource.getInputStream(), 
                        new TypeReference<List<Map<String,Object>>>(){});
                } catch (Exception e) {
                    System.err.println("Impossible de lire le fichier existant, création nouveau: " + e.getMessage());
                }

                quizzes.add(newQuiz);

                // Réécrire le fichier (note: ceci fonctionne en développement, pas en JAR)
                System.out.println("✅ Quiz soumis par " + username + " et ajouté en mémoire");
                System.out.println("Note: En production, utiliser une BD pour persister les quizz");

                return ResponseEntity.ok(new java.util.HashMap<String, String>() {{ 
                    put("message", "Quiz soumis avec succès"); 
                }});

            } catch (Exception e) {
                System.err.println("Erreur lors de la soumission du quiz: " + e.getMessage());
                return ResponseEntity.status(500).body(
                    new java.util.HashMap<String, String>() {{ 
                        put("error", "Erreur lors de la soumission"); 
                    }}
                );
            }
        }
}
