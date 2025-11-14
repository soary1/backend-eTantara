package hackathon.eTantara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tantara")
@CrossOrigin(origins = "*")
public class TantaraController {

    /**
     * Endpoint pour soumettre une histoire (tantara)
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitTantara(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String title = (String) request.get("title");
            String content = (String) request.get("content");

            if (username == null || title == null || content == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "username, title, and content are required");
                return ResponseEntity.badRequest().body(error);
            }

            // Créer l'histoire
            Map<String, Object> tantara = new HashMap<>();
            tantara.put("id", System.currentTimeMillis());
            tantara.put("title", title);
            tantara.put("content", content);
            tantara.put("submittedBy", username);
            tantara.put("submittedAt", System.currentTimeMillis());

            // Note: En production, sauvegarder dans la base de données
            System.out.println("✅ Histoire soumise par " + username + ": " + title);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Histoire soumise avec succès");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Erreur lors de la soumission de l'histoire: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la soumission");
            return ResponseEntity.status(500).body(error);
        }
    }
}
