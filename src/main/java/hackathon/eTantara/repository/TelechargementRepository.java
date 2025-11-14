package hackathon.eTantara.repository;

import hackathon.eTantara.entite.Telechargement;
import hackathon.eTantara.entite.ContenuCulturel;
import hackathon.eTantara.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelechargementRepository extends JpaRepository<Telechargement, Long> {

    /**
     * Trouve tous les téléchargements d’un utilisateur
     */
    List<Telechargement> findByUtilisateurOrderByDateTelechargementDesc(Utilisateur utilisateur);

    /**
     * Trouve tous les téléchargements d’un contenu culturel
     */
    List<Telechargement> findByContenuOrderByDateTelechargementDesc(ContenuCulturel contenu);

    /**
     * Vérifie si un utilisateur a déjà téléchargé un contenu
     */
    boolean existsByUtilisateurAndContenu(Utilisateur utilisateur, ContenuCulturel contenu);

    /**
     * Compte le nombre de téléchargements d’un contenu
     */
    @Query("SELECT COUNT(t) FROM Telechargement t WHERE t.contenu = :contenu")
    Long countByContenu(@Param("contenu") ContenuCulturel contenu);

    /**
     * Trouve les contenus les plus téléchargés (version JPQL correcte)
     */
    @Query("SELECT t.contenu, COUNT(t) FROM Telechargement t GROUP BY t.contenu ORDER BY COUNT(t) DESC")
    List<Object[]> findMostDownloadedContent();
}
