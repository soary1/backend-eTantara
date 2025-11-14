package hackathon.eTantara.service;

import hackathon.eTantara.entite.ContenuCulturel;
import hackathon.eTantara.entite.Categorie;
import hackathon.eTantara.repository.ContenuCulturelRepository;
import hackathon.eTantara.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class ContenuCulturelService {

    @Autowired
    private ContenuCulturelRepository contenuCulturelRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    /**
     * Retourne tous les contenus disponibles avec leurs catégories
     */
    public List<ContenuCulturel> getAllContenus() {
        return contenuCulturelRepository.findAllWithCategorie();
    }

    /**
     * Retourne toutes les catégories disponibles
     */
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    /**
     * Retourne les catégories qui ont du contenu
     */
    public List<Categorie> getCategoriesWithContent() {
        return categorieRepository.findCategoriesWithContent();
    }

    /**
     * Recherche des contenus par catégorie
     */
    public List<ContenuCulturel> getContenusByCategorie(String nomCategorie) {
        Optional<Categorie> categorieOpt = categorieRepository.findByNom(nomCategorie);
        if (categorieOpt.isPresent()) {
            return categorieOpt.get().getContenus();
        }
        return java.util.Collections.emptyList();
    }

    /**
     * 🔹 Lire le contenu d'un fichier texte associé à une histoire
     * Gère les chemins relatifs et les erreurs proprement.
     */
    public String lireFichierContenu(String cheminFichier) {
        try {
            // Gérer les chemins relatifs et absolus
            Path path = Paths.get(cheminFichier);
            if (!path.isAbsolute()) {
                path = Paths.get(System.getProperty("user.dir"), cheminFichier);
            }

            if (!Files.exists(path)) {
                return "⚠️ Fichier non trouvé : " + cheminFichier;
            }

            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);

        } catch (IOException e) {
            return "❌ Erreur lors de la lecture du fichier : " + e.getMessage();
        }
    }

    /**
     * Récupérer un contenu culturel complet (avec texte du fichier)
     */
    public Optional<ContenuCulturel> getContenuComplet(Long id) {
        return contenuCulturelRepository.findById(id);
    }
}
