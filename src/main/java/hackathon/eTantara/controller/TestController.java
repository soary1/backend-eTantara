package hackathon.eTantara.controller;

import hackathon.eTantara.dto.AuthResponse;
import hackathon.eTantara.dto.RegisterRequest;
import hackathon.eTantara.dto.SimpleRegisterRequest;
import hackathon.eTantara.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private AuthService authService;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "eTantara API fonctionne correctement !");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/db")
    public Map<String, String> testDb() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Base de données accessible");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @PostMapping("/register-test")
    public Map<String, Object> testRegister() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test de données d'inscription
            Map<String, String> testData = new HashMap<>();
            testData.put("nom", "Test");
            testData.put("prenom", "Utilisateur");
            testData.put("username", "testuser");
            testData.put("email", "test@example.com");
            testData.put("password", "123456");
            
            response.put("status", "Test data created");
            response.put("testData", testData);
            response.put("timestamp", java.time.LocalDateTime.now().toString());
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "error");
        }
        return response;
    }

    @PostMapping("/simple-register")
    public Map<String, Object> testSimpleRegister(@RequestBody SimpleRegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Test inscription - Nom: " + request.getNom());
            System.out.println("Test inscription - Username: " + request.getUsername());
            System.out.println("Test inscription - Email: " + request.getEmail());
            
            response.put("status", "Données reçues avec succès");
            response.put("nom", request.getNom());
            response.put("username", request.getUsername());
            response.put("email", request.getEmail());
            response.put("timestamp", java.time.LocalDateTime.now().toString());
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "error");
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/full-register")
    public Map<String, Object> testFullRegister(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== TEST INSCRIPTION COMPLÈTE ===");
            System.out.println("Nom: " + request.getNom());
            System.out.println("Prenom: " + request.getPrenom());
            System.out.println("Username: " + request.getUsername());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password length: " + (request.getPassword() != null ? request.getPassword().length() : "null"));
            
            // Tentative d'inscription via le service
            AuthResponse authResponse = authService.register(request);
            
            response.put("status", "Inscription réussie");
            response.put("userId", authResponse.getId());
            response.put("username", authResponse.getUsername());
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
        } catch (Exception e) {
            System.err.println("ERREUR lors du test d'inscription: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("status", "error");
        }
        return response;
    }

    @PostMapping("/no-validation-register")
    public Map<String, Object> testNoValidationRegister(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== TEST SANS VALIDATION ===");
            
            // Créer manuellement un RegisterRequest
            RegisterRequest request = new RegisterRequest();
            request.setNom(requestData.get("nom"));
            request.setPrenom(requestData.get("prenom"));
            request.setUsername(requestData.get("username"));
            request.setEmail(requestData.get("email"));
            request.setPassword(requestData.get("password"));
            
            System.out.println("Données créées manuellement, tentative d'inscription...");
            
            AuthResponse authResponse = authService.register(request);
            
            response.put("status", "Inscription réussie sans validation");
            response.put("userId", authResponse.getId());
            response.put("username", authResponse.getUsername());
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
        } catch (Exception e) {
            System.err.println("ERREUR test sans validation: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("status", "error");
        }
        return response;
    }

    @GetMapping("/db-test")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== TEST BASE DE DONNÉES ===");
            
            // Test simple de comptage
            long count = authService.countUsers();
            
            response.put("status", "Base de données accessible");
            response.put("userCount", count);
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
        } catch (Exception e) {
            System.err.println("ERREUR test DB: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("status", "error");
        }
        return response;
    }

    @PostMapping("/simple-user-create")
    public Map<String, Object> testSimpleUserCreate(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== TEST CRÉATION UTILISATEUR SIMPLE ===");
            
            String nom = requestData.get("nom");
            String prenom = requestData.get("prenom");
            String username = requestData.get("username");
            String email = requestData.get("email");
            String password = requestData.get("password");
            
            System.out.println("Appel createSimpleUser...");
            hackathon.eTantara.entite.Utilisateur user = authService.createSimpleUser(nom, prenom, username, email, password);
            
            response.put("status", "Utilisateur créé avec succès");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
        } catch (Exception e) {
            System.err.println("ERREUR test création simple: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("status", "error");
        }
        return response;
    }
}
