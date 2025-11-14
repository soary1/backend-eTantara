package hackathon.eTantara.service;

import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    /**
     * Récupérer tous les utilisateurs
     */
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }
    
    /**
     * Récupérer un utilisateur par son ID
     */
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    /**
     * Récupérer un utilisateur par son email
     */
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    
    /**
     * Récupérer un utilisateur par son username
     */
    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }
    
    /**
     * Sauvegarder un utilisateur
     */
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Supprimer un utilisateur
     */
    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    /**
     * Vérifier si un email existe
     */
    public boolean existsByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
    
    /**
     * Récupérer les utilisateurs par rôle
     */
    public List<Utilisateur> findByRole(String role) {
        return utilisateurRepository.findByRole(role);
    }
    
    /**
     * Mettre à jour les points d'un utilisateur
     */
    public Utilisateur updatePoints(Long userId, Integer points) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setPoints(utilisateur.getPoints() + points);
            return utilisateurRepository.save(utilisateur);
        }
        throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId);
    }
}
