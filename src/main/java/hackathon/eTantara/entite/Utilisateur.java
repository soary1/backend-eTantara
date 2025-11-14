package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Champs d'identité
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 100)
    private String prenom; // ⚙️ ajouté pour compatibilité avec AuthService

    @Column(nullable = false, unique = true, length = 100)
    private String username; // ⚙️ ajouté pour compatibilité avec AuthService (nom d'utilisateur distinct de l'email)

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    private Integer points = 0;

    @Column(length = 50)
    private String role = "USER";

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Relations avec d'autres entités
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoirePartagee> histoiresPartagees = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreUtilisateur> scores = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telechargement> telechargements = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ecoute> ecoutes = new ArrayList<>();

    // ✅ Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String username, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // ✅ Getters & Setters
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

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public List<HistoirePartagee> getHistoiresPartagees() { return histoiresPartagees; }
    public void setHistoiresPartagees(List<HistoirePartagee> histoiresPartagees) { this.histoiresPartagees = histoiresPartagees; }

    public List<ScoreUtilisateur> getScores() { return scores; }
    public void setScores(List<ScoreUtilisateur> scores) { this.scores = scores; }

    public List<Telechargement> getTelechargements() { return telechargements; }
    public void setTelechargements(List<Telechargement> telechargements) { this.telechargements = telechargements; }

    public List<Ecoute> getEcoutes() { return ecoutes; }
    public void setEcoutes(List<Ecoute> ecoutes) { this.ecoutes = ecoutes; }
}
