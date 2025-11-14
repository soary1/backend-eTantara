package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "calendrier_culturel")
public class CalendrierCulturel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_evenement", nullable = false)
    private LocalDate dateEvenement;

    @Column(length = 100)
    private String region;

    @Column(name = "type_evenement", length = 100)
    private String typeEvenement;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getTypeEvenement() { return typeEvenement; }
    public void setTypeEvenement(String typeEvenement) { this.typeEvenement = typeEvenement; }
}
