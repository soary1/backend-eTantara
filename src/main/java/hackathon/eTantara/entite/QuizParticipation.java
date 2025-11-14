package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "quiz_participation")
public class QuizParticipation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    
    @Column(name = "date_quiz", nullable = false)
    private LocalDate dateQuiz;
    
    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;
    
    @Column(name = "reponse_donnee")
    private String reponseDonnee;
    
    @Column(name = "est_correct", nullable = false)
    private Boolean estCorrect;
    
    @Column(name = "points_obtenus", nullable = false)
    private Integer pointsObtenus;
    
    @Column(name = "date_participation", nullable = false)
    private java.time.LocalDateTime dateParticipation;
    
    // Constructeurs
    public QuizParticipation() {
        this.dateParticipation = java.time.LocalDateTime.now();
    }
    
    public QuizParticipation(Utilisateur utilisateur, LocalDate dateQuiz, Integer quizId, 
                           String reponseDonnee, Boolean estCorrect, Integer pointsObtenus) {
        this();
        this.utilisateur = utilisateur;
        this.dateQuiz = dateQuiz;
        this.quizId = quizId;
        this.reponseDonnee = reponseDonnee;
        this.estCorrect = estCorrect;
        this.pointsObtenus = pointsObtenus;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public LocalDate getDateQuiz() {
        return dateQuiz;
    }
    
    public void setDateQuiz(LocalDate dateQuiz) {
        this.dateQuiz = dateQuiz;
    }
    
    public Integer getQuizId() {
        return quizId;
    }
    
    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }
    
    public String getReponseDonnee() {
        return reponseDonnee;
    }
    
    public void setReponseDonnee(String reponseDonnee) {
        this.reponseDonnee = reponseDonnee;
    }
    
    public Boolean getEstCorrect() {
        return estCorrect;
    }
    
    public void setEstCorrect(Boolean estCorrect) {
        this.estCorrect = estCorrect;
    }
    
    public Integer getPointsObtenus() {
        return pointsObtenus;
    }
    
    public void setPointsObtenus(Integer pointsObtenus) {
        this.pointsObtenus = pointsObtenus;
    }
    
    public java.time.LocalDateTime getDateParticipation() {
        return dateParticipation;
    }
    
    public void setDateParticipation(java.time.LocalDateTime dateParticipation) {
        this.dateParticipation = dateParticipation;
    }
}
