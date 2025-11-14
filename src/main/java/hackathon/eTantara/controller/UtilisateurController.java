package hackathon.eTantara.controller;

import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UtilisateurController {
    
    @Autowired
    private UtilisateurService utilisateurService;
    
    /**
     * Récupérer le profil de l'utilisateur connecté
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);
            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                // Ne pas retourner le mot de passe
                utilisateur.setMotDePasse(null);
                return ResponseEntity.ok(utilisateur);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Erreur: " + e.getMessage()));
        }
    }
    
    /**
     * Mettre à jour les points de l'utilisateur
     */
    @PutMapping("/points/{userId}")
    public ResponseEntity<?> updateUserPoints(@PathVariable Long userId, @RequestParam Integer points) {
        try {
            Utilisateur utilisateur = utilisateurService.updatePoints(userId, points);
            utilisateur.setMotDePasse(null); // Ne pas retourner le mot de passe
            return ResponseEntity.ok(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Erreur: " + e.getMessage()));
        }
    }

    /**
     * Récupérer les points par username (endpoint public)
     */
    @GetMapping("/points/{username}")
    public ResponseEntity<?> getUserPointsByUsername(@PathVariable String username) {
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurService.findByUsername(username);
            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                return ResponseEntity.ok(new java.util.HashMap<String, Integer>() {{
                    put("points", utilisateur.getPoints());
                }});
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Erreur: " + e.getMessage()));
        }
    }

    /**
     * Mettre à jour le profil de l'utilisateur (support minimal pour frontend)
     * Accepte JSON avec au moins un des champs: id, username ou email pour identifier l'utilisateur.
     * Met à jour les champs présents (nom, prenom, username, email, points).
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody java.util.Map<String, Object> payload) {
        try {
            Optional<Utilisateur> utilisateurOpt = Optional.empty();

            // Tenter par id
            if (payload.get("id") != null) {
                try {
                    Long id = Long.valueOf(String.valueOf(payload.get("id")));
                    utilisateurOpt = utilisateurService.findById(id);
                } catch (NumberFormatException ignored) {}
            }

            // Sinon par username
            if (!utilisateurOpt.isPresent() && payload.get("username") != null) {
                utilisateurOpt = utilisateurService.findByUsername(String.valueOf(payload.get("username")));
            }

            // Sinon par email
            if (!utilisateurOpt.isPresent() && payload.get("email") != null) {
                utilisateurOpt = utilisateurService.findByEmail(String.valueOf(payload.get("email")));
            }

            if (!utilisateurOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Utilisateur utilisateur = utilisateurOpt.get();

            if (payload.containsKey("nom")) utilisateur.setNom(String.valueOf(payload.get("nom")));
            if (payload.containsKey("prenom")) utilisateur.setPrenom(String.valueOf(payload.get("prenom")));
            if (payload.containsKey("username")) utilisateur.setUsername(String.valueOf(payload.get("username")));
            if (payload.containsKey("email")) utilisateur.setEmail(String.valueOf(payload.get("email")));
            if (payload.containsKey("points")) {
                try {
                    utilisateur.setPoints(Integer.valueOf(String.valueOf(payload.get("points"))));
                } catch (NumberFormatException ignored) {}
            }

            Utilisateur saved = utilisateurService.save(utilisateur);
            // Ne pas renvoyer le mot de passe
            saved.setMotDePasse(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Erreur: " + e.getMessage()));
        }
    }

}
