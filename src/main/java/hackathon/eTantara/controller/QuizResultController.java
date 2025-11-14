package hackathon.eTantara.controller;

import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizResultController {

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Endpoint pour enregistrer les résultats du quiz et mettre à jour les points
     * Reçoit: { pseudo: string, score: number, total: number }
     */
    @PostMapping("/resultats")
    public ResponseEntity<?> saveQuizResult(@RequestBody Map<String, Object> request) {
        try {
            String pseudo = (String) request.get("pseudo");
            Object scoreObj = request.get("score");
            Object totalObj = request.get("total");

            if (pseudo == null || scoreObj == null || totalObj == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "pseudo, score, and total are required");
                return ResponseEntity.badRequest().body(error);
            }

            int score = 0;
            int total = 0;

            try {
                if (scoreObj instanceof Number) {
                    score = ((Number) scoreObj).intValue();
                } else {
                    score = Integer.parseInt(scoreObj.toString());
                }

                if (totalObj instanceof Number) {
                    total = ((Number) totalObj).intValue();
                } else {
                    total = Integer.parseInt(totalObj.toString());
                }
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid score or total format");
                return ResponseEntity.badRequest().body(error);
            }

            // Trouver l'utilisateur par username
            Optional<Utilisateur> utilisateurOpt = utilisateurService.findByUsername(pseudo);

            if (!utilisateurOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utilisateur non trouvé: " + pseudo);
                return ResponseEntity.status(404).body(error);
            }

            Utilisateur utilisateur = utilisateurOpt.get();

            // Calculer les points à ajouter : 10 points par bonne réponse
            int pointsGagnes = score * 10;

            // Ajouter les points à l'utilisateur
            utilisateur.setPoints(utilisateur.getPoints() + pointsGagnes);

            // Sauvegarder l'utilisateur avec les nouveaux points
            utilisateurService.save(utilisateur);

            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Résultats enregistrés avec succès");
            response.put("pseudo", pseudo);
            response.put("score", score);
            response.put("total", total);
            response.put("pointsGagnes", pointsGagnes);
            response.put("pointsTotal", utilisateur.getPoints());

            System.out.println("✅ Quiz terminé par " + pseudo + ": " + score + "/" + total + 
                             " | Points gagnés: " + pointsGagnes + " | Points total: " + utilisateur.getPoints());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement des résultats: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de l'enregistrement des résultats: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
