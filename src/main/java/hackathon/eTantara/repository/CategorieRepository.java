package hackathon.eTantara.repository;

import hackathon.eTantara.entite.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    
    /**
     * Trouve une catégorie par son nom
     */
    Optional<Categorie> findByNom(String nom);
    
    /**
     * Trouve toutes les catégories qui ont du contenu
     */
    @Query("SELECT DISTINCT c FROM Categorie c JOIN c.contenus")
    List<Categorie> findCategoriesWithContent();
}
