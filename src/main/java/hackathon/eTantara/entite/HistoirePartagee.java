package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "histoire_partagee")
public class HistoirePartagee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(length = 100)
    private String region;

    @Column(name = "fichier_audio", length = 255)
    private String fichierAudio;

    @Column(name = "date_partage")
    private LocalDateTime datePartage = LocalDateTime.now();

    @Column(name = "est_valide")
    private Boolean estValide = Boolean.FALSE;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getFichierAudio() { return fichierAudio; }
    public void setFichierAudio(String fichierAudio) { this.fichierAudio = fichierAudio; }
    public LocalDateTime getDatePartage() { return datePartage; }
    public void setDatePartage(LocalDateTime datePartage) { this.datePartage = datePartage; }
    public Boolean getEstValide() { return estValide; }
    public void setEstValide(Boolean estValide) { this.estValide = estValide; }
}
