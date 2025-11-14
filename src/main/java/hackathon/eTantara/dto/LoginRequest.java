package hackathon.eTantara.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    
    // Constructeurs
    public LoginRequest() {}
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters et setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // Pour la compatibilité avec l'ancien code
    public String getEmail() { return username; }
    public void setEmail(String email) { this.username = email; }
    public String getMotDePasse() { return password; }
    public void setMotDePasse(String motDePasse) { this.password = motDePasse; }
}
