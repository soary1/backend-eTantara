package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ecoute")
public class Ecoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenu_id")
    private ContenuCulturel contenu;

    @Column(name = "date_ecoute")
    private LocalDateTime dateEcoute = LocalDateTime.now();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public ContenuCulturel getContenu() { return contenu; }
    public void setContenu(ContenuCulturel contenu) { this.contenu = contenu; }
    public LocalDateTime getDateEcoute() { return dateEcoute; }
    public void setDateEcoute(LocalDateTime dateEcoute) { this.dateEcoute = dateEcoute; }
}
