package hackathon.eTantara.entite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Column(name = "reponse_correcte", nullable = false, length = 255)
    private String reponseCorrecte;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponsePossible> reponsesPossibles = new ArrayList<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }
    public String getReponseCorrecte() { return reponseCorrecte; }
    public void setReponseCorrecte(String reponseCorrecte) { this.reponseCorrecte = reponseCorrecte; }
    public List<ReponsePossible> getReponsesPossibles() { return reponsesPossibles; }
    public void setReponsesPossibles(List<ReponsePossible> reponsesPossibles) { this.reponsesPossibles = reponsesPossibles; }
}
