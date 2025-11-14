package hackathon.eTantara.entite;

import javax.persistence.*;

@Entity
@Table(name = "reponse_possibles")
public class ReponsePossible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(length = 255)
    private String texte;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }
}
