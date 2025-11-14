package hackathon.eTantara.controller;

import hackathon.eTantara.dto.AuthResponse;
import hackathon.eTantara.dto.LoginRequest;
import hackathon.eTantara.dto.RegisterRequest;
import hackathon.eTantara.service.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            System.out.println("Tentative d'inscription pour: " + registerRequest.getUsername());
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Erreur interne du serveur: " + e.getMessage()));
        }
    }
    
    /**
     * Connexion d'un utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: " + e.getMessage()));
        }
    }
    
    /**
     * Test d'inscription sans validation
     */
    @PostMapping("/register-debug")
    public ResponseEntity<?> registerDebug(@RequestBody Map<String, String> requestData) {
        try {
            System.out.println("=== DEBUG INSCRIPTION DIRECT ===");
            System.out.println("Données reçues: " + requestData);
            
            // Créer manuellement un RegisterRequest
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setNom(requestData.get("nom"));
            registerRequest.setPrenom(requestData.get("prenom"));
            registerRequest.setUsername(requestData.get("username"));
            registerRequest.setEmail(requestData.get("email"));
            registerRequest.setPassword(requestData.get("password"));
            
            System.out.println("RegisterRequest créé, appel du service...");
            AuthResponse response = authService.register(registerRequest);
            System.out.println("Service appelé avec succès !");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Erreur Runtime lors du debug: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur Runtime: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erreur générale lors du debug: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Erreur interne: " + e.getMessage()));
        }
    }
    
    /**
     * Inscription temporaire SANS validation pour debug
     */
    @PostMapping("/register-no-validation")
    public ResponseEntity<?> registerNoValidation(@RequestBody RegisterRequest registerRequest) {
        try {
            System.out.println("=== INSCRIPTION SANS @Valid ===");
            System.out.println("Nom: " + registerRequest.getNom());
            System.out.println("Prenom: " + registerRequest.getPrenom());
            System.out.println("Username: " + registerRequest.getUsername());
            System.out.println("Email: " + registerRequest.getEmail());
            System.out.println("Password length: " + (registerRequest.getPassword() != null ? registerRequest.getPassword().length() : "null"));
            
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de l'inscription sans validation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erreur inattendue sans validation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Erreur interne du serveur: " + e.getMessage()));
        }
    }
    
    /**
     * Test ultra-simple sans aucune vérification
     */
    @PostMapping("/register-ultra-simple")
    public ResponseEntity<?> registerUltraSimple(@RequestBody Map<String, String> requestData) {
        try {
            System.out.println("=== TEST ULTRA SIMPLE ===");
            System.out.println("Création directe sans vérifications...");
            
            String nom = requestData.get("nom");
            String prenom = requestData.get("prenom");
            String username = requestData.get("username") + System.currentTimeMillis(); // Rendre unique
            String email = System.currentTimeMillis() + "@test.com"; // Email unique
            String password = requestData.get("password");
            
            hackathon.eTantara.entite.Utilisateur user = authService.createSimpleUser(nom, prenom, username, email, password);
            
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("status", "Inscription ultra-simple réussie");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erreur ultra-simple: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Erreur ultra-simple: " + e.getMessage()));
        }
    }
    
    /**
     * Test basique base de données
     */
    @PostMapping("/test-db-basic")
    public ResponseEntity<?> testDbBasic() {
        try {
            System.out.println("=== TEST DB BASIQUE ===");
            
            // Test simple : compter les utilisateurs
            long count = authService.countUsers();
            System.out.println("Nombre d'utilisateurs: " + count);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Test DB réussi");
            response.put("userCount", count);
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erreur test DB basique: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Erreur DB: " + e.getMessage()));
        }
    }
    
    /**
     * Classe pour les réponses de message
     */
    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
