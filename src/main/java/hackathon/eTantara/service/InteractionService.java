package hackathon.eTantara.service;

import hackathon.eTantara.entite.*;
import hackathon.eTantara.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {

    @Autowired
    private EcouteRepository ecouteRepository;
    
    @Autowired
    private TelechargementRepository telechargementRepository;
    
    @Autowired
    private ContenuCulturelRepository contenuCulturelRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Enregistre une écoute d'un contenu culturel
     */
    public boolean enregistrerEcoute(String emailUtilisateur, Long contenuId) {
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(emailUtilisateur);
            Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
            
            if (utilisateurOpt.isPresent() && contenuOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                ContenuCulturel contenu = contenuOpt.get();
                
                // Créer une nouvelle écoute
                Ecoute ecoute = new Ecoute();
                ecoute.setUtilisateur(utilisateur);
                ecoute.setContenu(contenu);
                
                ecouteRepository.save(ecoute);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement de l'écoute: " + e.getMessage());
            return false;
        }
    }

    /**
     * Enregistre un téléchargement d'un contenu culturel
     */
    public boolean enregistrerTelechargement(String emailUtilisateur, Long contenuId) {
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(emailUtilisateur);
            Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
            
            if (utilisateurOpt.isPresent() && contenuOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                ContenuCulturel contenu = contenuOpt.get();
                
                // Créer un nouveau téléchargement
                Telechargement telechargement = new Telechargement();
                telechargement.setUtilisateur(utilisateur);
                telechargement.setContenu(contenu);
                
                telechargementRepository.save(telechargement);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du téléchargement: " + e.getMessage());
            return false;
        }
    }

    /**
     * Télécharge un fichier audio
     */
    public ResponseEntity<Resource> telechargerAudio(Long contenuId) {
        try {
            Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
            
            if (!contenuOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContenuCulturel contenu = contenuOpt.get();
            String cheminAudio = contenu.getFichierAudio();
            
            if (cheminAudio == null || cheminAudio.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Path fichierPath = Paths.get(cheminAudio);
            if (!fichierPath.isAbsolute()) {
                fichierPath = Paths.get(System.getProperty("user.dir"), cheminAudio);
            }
            Resource resource = new UrlResource(fichierPath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(fichierPath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Télécharge le contenu textuel
     */
    public ResponseEntity<Resource> telechargerContenu(Long contenuId) {
        try {
            Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
            
            if (!contenuOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContenuCulturel contenu = contenuOpt.get();
            String cheminContenu = contenu.getFichierContenu();
            
            if (cheminContenu == null || cheminContenu.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Path fichierPath = Paths.get(cheminContenu);
            if (!fichierPath.isAbsolute()) {
                fichierPath = Paths.get(System.getProperty("user.dir"), cheminContenu);
            }
            Resource resource = new UrlResource(fichierPath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + contenu.getTitre() + ".txt\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtient l'historique des écoutes d'un utilisateur
     */
    public List<Ecoute> getHistoriqueEcoutes(String emailUtilisateur) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(emailUtilisateur);
        if (utilisateurOpt.isPresent()) {
            return ecouteRepository.findByUtilisateurOrderByDateEcouteDesc(utilisateurOpt.get());
        }
        return Collections.emptyList();
    }

    /**
     * Obtient l'historique des téléchargements d'un utilisateur
     */
    public List<Telechargement> getHistoriqueTelechargements(String emailUtilisateur) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(emailUtilisateur);
        if (utilisateurOpt.isPresent()) {
            return telechargementRepository.findByUtilisateurOrderByDateTelechargementDesc(utilisateurOpt.get());
        }
        return Collections.emptyList();
    }

    /**
     * Obtient les statistiques d'un contenu
     */
    public Long getNombreEcoutes(Long contenuId) {
        Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
        if (contenuOpt.isPresent()) {
            return ecouteRepository.countByContenu(contenuOpt.get());
        }
        return 0L;
    }

    public Long getNombreTelechargements(Long contenuId) {
        Optional<ContenuCulturel> contenuOpt = contenuCulturelRepository.findById(contenuId);
        if (contenuOpt.isPresent()) {
            return telechargementRepository.countByContenu(contenuOpt.get());
        }
        return 0L;
    }
}
