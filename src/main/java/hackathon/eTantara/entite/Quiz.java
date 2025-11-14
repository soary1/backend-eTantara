package hackathon.eTantara.entite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String titre;

    @Column(name = "type_quiz", length = 50)
    private String typeQuiz;

    @Column(length = 50)
    private String difficulte;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreUtilisateur> scores = new ArrayList<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getTypeQuiz() { return typeQuiz; }
    public void setTypeQuiz(String typeQuiz) { this.typeQuiz = typeQuiz; }
    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public List<ScoreUtilisateur> getScores() { return scores; }
    public void setScores(List<ScoreUtilisateur> scores) { this.scores = scores; }
}
