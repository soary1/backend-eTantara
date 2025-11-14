package hackathon.eTantara.repository;

import hackathon.eTantara.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    /**
     * Trouve un utilisateur par son email
     */
    Optional<Utilisateur> findByEmail(String email);
    
    /**
     * Trouve un utilisateur par son nom d'utilisateur
     */
    Optional<Utilisateur> findByUsername(String username);
    
    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);
    
    /**
     * Vérifie si un nom d'utilisateur existe déjà
     */
    boolean existsByUsername(String username);
    
    /**
     * Trouve tous les utilisateurs par rôle
     */
    java.util.List<Utilisateur> findByRole(String role);
}
