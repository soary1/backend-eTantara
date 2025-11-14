package hackathon.eTantara.repository;

import hackathon.eTantara.entite.QuizParticipation;
import hackathon.eTantara.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizParticipationRepository extends JpaRepository<QuizParticipation, Long> {
    
    /**
     * Vérifier si un utilisateur a déjà participé au quiz d'une date donnée
     */
    Optional<QuizParticipation> findByUtilisateurAndDateQuiz(Utilisateur utilisateur, LocalDate dateQuiz);
    
    /**
     * Récupérer toutes les participations d'un utilisateur
     */
    List<QuizParticipation> findByUtilisateurOrderByDateParticipationDesc(Utilisateur utilisateur);
    
    /**
     * Compter le nombre de bonnes réponses d'un utilisateur
     */
    long countByUtilisateurAndEstCorrect(Utilisateur utilisateur, Boolean estCorrect);
    
    /**
     * Récupérer les participations d'un utilisateur pour une période
     */
    List<QuizParticipation> findByUtilisateurAndDateQuizBetween(Utilisateur utilisateur, LocalDate dateDebut, LocalDate dateFin);
}
