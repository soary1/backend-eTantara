package hackathon.eTantara.dto;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String role;
    private Integer points;
    
    // Constructeurs
    public AuthResponse() {}
    
    public AuthResponse(String token, Long id, String nom, String prenom, String username, String email, String role, Integer points) {
        this.token = token;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.email = email;
        this.role = role;
        this.points = points;
    }
    
    // Getters et setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
