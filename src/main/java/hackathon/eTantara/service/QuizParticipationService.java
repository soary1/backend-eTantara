package hackathon.eTantara.service;

import hackathon.eTantara.entite.QuizParticipation;
import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.repository.QuizParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuizParticipationService {
    
    @Autowired
    private QuizParticipationRepository quizParticipationRepository;
    
    /**
     * Vérifier si un utilisateur a déjà participé au quiz du jour
     */
    public boolean aDejaParticipeAujourdHui(Utilisateur utilisateur) {
        LocalDate aujourdHui = LocalDate.now();
        Optional<QuizParticipation> participation = quizParticipationRepository
            .findByUtilisateurAndDateQuiz(utilisateur, aujourdHui);
        return participation.isPresent();
    }
    
    /**
     * Enregistrer une participation au quiz
     */
    public QuizParticipation enregistrerParticipation(Utilisateur utilisateur, Integer quizId, 
                                                     String reponseDonnee, Boolean estCorrect, Integer pointsObtenus) {
        LocalDate aujourdHui = LocalDate.now();
        
        QuizParticipation participation = new QuizParticipation(
            utilisateur, aujourdHui, quizId, reponseDonnee, estCorrect, pointsObtenus
        );
        
        return quizParticipationRepository.save(participation);
    }
    
    /**
     * Récupérer la participation du jour d'un utilisateur
     */
    public Optional<QuizParticipation> getParticipationDuJour(Utilisateur utilisateur) {
        LocalDate aujourdHui = LocalDate.now();
        return quizParticipationRepository.findByUtilisateurAndDateQuiz(utilisateur, aujourdHui);
    }
    
    /**
     * Récupérer toutes les participations d'un utilisateur
     */
    public List<QuizParticipation> getParticipationsUtilisateur(Utilisateur utilisateur) {
        return quizParticipationRepository.findByUtilisateurOrderByDateParticipationDesc(utilisateur);
    }
    
    /**
     * Compter le nombre de bonnes réponses d'un utilisateur
     */
    public long getNombreBonnesReponses(Utilisateur utilisateur) {
        return quizParticipationRepository.countByUtilisateurAndEstCorrect(utilisateur, true);
    }
    
    /**
     * Compter le nombre total de participations d'un utilisateur
     */
    public long getNombreTotalParticipations(Utilisateur utilisateur) {
        return quizParticipationRepository.findByUtilisateurOrderByDateParticipationDesc(utilisateur).size();
    }
}
