package hackathon.eTantara.service;

import hackathon.eTantara.dto.AuthResponse;
import hackathon.eTantara.dto.LoginRequest;
import hackathon.eTantara.dto.RegisterRequest;
import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // ===============================
    // 🔹 INSCRIPTION
    // ===============================
    public AuthResponse register(RegisterRequest request) {
        System.out.println("=== DÉBUT INSCRIPTION ===");

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        if (utilisateurRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setUsername(request.getUsername());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getPassword())); // encodage
        utilisateur.setRole("USER");
        utilisateur.setPoints(0);
        utilisateur.setDateCreation(LocalDateTime.now());

        utilisateur = utilisateurRepository.save(utilisateur);
        String token = jwtService.generateToken(utilisateur.getEmail());

        System.out.println("=== INSCRIPTION OK ===");
        return new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getUsername(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getPoints()
        );
    }

    // ===============================
    // 🔹 CONNEXION
    // ===============================
    public AuthResponse login(LoginRequest request) {
        System.out.println("=== DÉBUT CONNEXION ===");

        // Trouver par username ou email
        Optional<Utilisateur> optUtilisateur = utilisateurRepository.findByUsername(request.getUsername());
        if (!optUtilisateur.isPresent()) {
            optUtilisateur = utilisateurRepository.findByEmail(request.getUsername());
        }

        if (!optUtilisateur.isPresent()) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        Utilisateur utilisateur = optUtilisateur.get();

        // Vérifier le mot de passe avec BCrypt
        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // Générer un token JWT
        String token = jwtService.generateToken(utilisateur.getEmail());

        System.out.println("=== CONNEXION OK ===");
        return new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getUsername(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getPoints()
        );
    }

    // ===============================
    // 🔹 Méthodes utilitaires
    // ===============================
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public long countUsers() {
        return utilisateurRepository.count();
    }

    public Utilisateur createSimpleUser(String nom, String prenom, String username, String email, String password) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(password));
        utilisateur.setRole("USER");
        utilisateur.setPoints(0);
        utilisateur.setDateCreation(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }
    
}
