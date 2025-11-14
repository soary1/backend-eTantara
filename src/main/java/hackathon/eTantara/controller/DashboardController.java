package hackathon.eTantara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @GetMapping
    public ResponseEntity<?> getDashboardInfo(Authentication authentication) {
        // ✅ Récupère l'utilisateur actuellement connecté (grâce au JWT) ou utilisateur anonyme
        String email = "anonyme@example.com";
        String username = "Visiteur";
        
        if (authentication != null && authentication.getName() != null) {
            email = authentication.getName();
            username = email.split("@")[0];
            System.out.println("✅ Dashboard access for authenticated user: " + email);
        } else {
            System.out.println("✅ Dashboard access for anonymous user");
        }

        // 🔹 Exemple de données fictives
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("points", 120);
        data.put("message", "Bienvenue sur ton tableau de bord, " + username + " !");

        // 🔹 Liste d’activités récentes (compatible Java 8)
        List<Map<String, String>> activities = new ArrayList<>();

        Map<String, String> act1 = new HashMap<>();
        act1.put("title", "Nouvelle histoire ajoutée");
        act1.put("description", "Les contes de grand-mère Rasoa");
        act1.put("time", "Il y a 2 heures");
        act1.put("badge", "Nouveau");
        act1.put("badgeClass", "bg-primary");
        activities.add(act1);

        Map<String, String> act2 = new HashMap<>();
        act2.put("title", "Quiz complété");
        act2.put("description", "Culture malagasy - Score : 88%");
        act2.put("time", "Hier");
        act2.put("badge", "Succès");
        act2.put("badgeClass", "bg-success");
        activities.add(act2);

        Map<String, String> act3 = new HashMap<>();
        act3.put("title", "Audio écouté");
        act3.put("description", "Hira gasy traditionnel");
        act3.put("time", "Il y a 3 jours");
        act3.put("badge", "Écouté");
        act3.put("badgeClass", "bg-info");
        activities.add(act3);

        data.put("activities", activities);

        return ResponseEntity.ok(data);
    }
}
