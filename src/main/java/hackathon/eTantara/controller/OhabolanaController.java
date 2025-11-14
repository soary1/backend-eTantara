package hackathon.eTantara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ohabolana")
@CrossOrigin(origins = "*")
public class OhabolanaController {

    /**
     * Endpoint pour soumettre un ohabolana (proverbe/expression)
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitOhabolana(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String expression = (String) request.get("expression");
            String meaning = (String) request.get("meaning");

            if (username == null || expression == null || meaning == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "username, expression, and meaning are required");
                return ResponseEntity.badRequest().body(error);
            }

            // Créer l'ohabolana
            Map<String, Object> ohabolana = new HashMap<>();
            ohabolana.put("id", System.currentTimeMillis());
            ohabolana.put("expression", expression);
            ohabolana.put("meaning", meaning);
            ohabolana.put("submittedBy", username);
            ohabolana.put("submittedAt", System.currentTimeMillis());

            // Note: En production, sauvegarder dans la base de données
            System.out.println("✅ Ohabolana soumis par " + username + ": " + expression);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Ohabolana soumis avec succès");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Erreur lors de la soumission de l'ohabolana: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la soumission");
            return ResponseEntity.status(500).body(error);
        }
    }
}
