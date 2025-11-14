package hackathon.eTantara.repository;

import hackathon.eTantara.entite.ContenuCulturel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContenuCulturelRepository extends JpaRepository<ContenuCulturel, Long> {
    
    @Query("SELECT c FROM ContenuCulturel c LEFT JOIN FETCH c.categorie")
    List<ContenuCulturel> findAllWithCategorie();
}
