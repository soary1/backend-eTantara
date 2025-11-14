package hackathon.eTantara.controller;

import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UtilisateurStatsController {

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Récupérer les statistiques de l'utilisateur connecté
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Utilisateur non authentifié");
            return ResponseEntity.status(401).body(error);
        }
        
        String email = authentication.getName();
        
        // Récupérer l'utilisateur depuis la base de données
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
        
        if (!utilisateurOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(404).body(error);
        }
        
        Utilisateur utilisateur = utilisateurOpt.get();
        Map<String, Object> stats = new HashMap<>();
        
        // Utiliser le vrai score depuis la base de données
        int score = utilisateur.getPoints();
        
        stats.put("email", utilisateur.getEmail());
        stats.put("username", utilisateur.getUsername() != null ? utilisateur.getUsername() : email.split("@")[0]);
        stats.put("nom", utilisateur.getNom());
        stats.put("prenom", utilisateur.getPrenom());
        stats.put("score", score);
        stats.put("niveau", getNiveauFromScore(score));
        
        // TODO: Implémenter les vraies statistiques d'activité depuis la base de données
        // Pour l'instant, utiliser des valeurs par défaut
        stats.put("contenusLus", 0);
        stats.put("contenusEcoutes", 0);
        stats.put("quizCompletes", 0);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Mettre à jour le score de l'utilisateur connecté
     */
    @PostMapping("/update-score")
    public ResponseEntity<?> updateUserScore(@RequestBody Map<String, Integer> request, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Utilisateur non authentifié");
            return ResponseEntity.status(401).body(error);
        }
        
        String email = authentication.getName();
        Integer pointsToAdd = request.get("points");
        
        if (pointsToAdd == null || pointsToAdd <= 0) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Points invalides");
            return ResponseEntity.status(400).body(error);
        }
        
        // Récupérer l'utilisateur depuis la base de données
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
        
        if (!utilisateurOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(404).body(error);
        }
        
        Utilisateur utilisateur = utilisateurOpt.get();
        
        // Ajouter les points au score actuel
        int nouveauScore = utilisateur.getPoints() + pointsToAdd;
        utilisateur.setPoints(nouveauScore);
        
        // Sauvegarder les modifications
        utilisateurService.save(utilisateur);
        
        // Retourner les nouvelles statistiques
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Score mis à jour avec succès");
        response.put("newScore", nouveauScore);
        response.put("pointsAdded", pointsToAdd);
        response.put("niveau", getNiveauFromScore(nouveauScore));
        
        return ResponseEntity.ok(response);
    }
    
    private String getNiveauFromScore(int score) {
        if (score < 50) return "Débutant";
        else if (score < 150) return "Intermédiaire";
        else if (score < 300) return "Avancé";
        else return "Expert";
    }
}
