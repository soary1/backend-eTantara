package hackathon.eTantara.repository;

import hackathon.eTantara.entite.Ecoute;
import hackathon.eTantara.entite.ContenuCulturel;
import hackathon.eTantara.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcouteRepository extends JpaRepository<Ecoute, Long> {

    /**
     * Trouve toutes les écoutes d'un utilisateur
     */
    List<Ecoute> findByUtilisateurOrderByDateEcouteDesc(Utilisateur utilisateur);

    /**
     * Trouve toutes les écoutes d'un contenu culturel
     */
    List<Ecoute> findByContenuOrderByDateEcouteDesc(ContenuCulturel contenu);

    /**
     * Vérifie si un utilisateur a déjà écouté un contenu
     */
    boolean existsByUtilisateurAndContenu(Utilisateur utilisateur, ContenuCulturel contenu);

    /**
     * Compte le nombre d'écoutes d'un contenu
     */
    @Query("SELECT COUNT(e) FROM Ecoute e WHERE e.contenu = :contenu")
    Long countByContenu(@Param("contenu") ContenuCulturel contenu);

    /**
     * Trouve les contenus les plus écoutés (compatible JPQL / Hibernate 5)
     */
    @Query("SELECT e.contenu, COUNT(e) FROM Ecoute e GROUP BY e.contenu ORDER BY COUNT(e) DESC")
    List<Object[]> findMostListenedContent();
}
