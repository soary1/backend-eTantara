package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contenu_culturel")
public class ContenuCulturel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @Column(name = "fichier_contenu", nullable = false, length = 255)
    private String fichierContenu;

    @Column(name = "fichier_audio", length = 255)
    private String fichierAudio;

    @Column(length = 100)
    private String region;

    @Column(name = "niveau_difficulte", length = 50)
    private String niveauDifficulte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @Column(length = 100)
    private String auteur;

    @Column(name = "est_valide")
    private Boolean estValide = Boolean.TRUE;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout = LocalDateTime.now();

    @OneToMany(mappedBy = "contenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telechargement> telechargements = new ArrayList<>();

    @OneToMany(mappedBy = "contenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ecoute> ecoutes = new ArrayList<>();

    

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
    public String getFichierContenu() { return fichierContenu; }
    public void setFichierContenu(String fichierContenu) { this.fichierContenu = fichierContenu; }
    public String getFichierAudio() { return fichierAudio; }
    public void setFichierAudio(String fichierAudio) { this.fichierAudio = fichierAudio; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getNiveauDifficulte() { return niveauDifficulte; }
    public void setNiveauDifficulte(String niveauDifficulte) { this.niveauDifficulte = niveauDifficulte; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public Boolean getEstValide() { return estValide; }
    public void setEstValide(Boolean estValide) { this.estValide = estValide; }
    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
    public List<Telechargement> getTelechargements() { return telechargements; }
    public void setTelechargements(List<Telechargement> telechargements) { this.telechargements = telechargements; }
    public List<Ecoute> getEcoutes() { return ecoutes; }
    public void setEcoutes(List<Ecoute> ecoutes) { this.ecoutes = ecoutes; }
}
