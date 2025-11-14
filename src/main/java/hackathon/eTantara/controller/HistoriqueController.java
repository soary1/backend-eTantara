package hackathon.eTantara.controller;

import hackathon.eTantara.entite.Ecoute;
import hackathon.eTantara.entite.Telechargement;
import hackathon.eTantara.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historique")
@CrossOrigin(origins = "*")
public class HistoriqueController {

    @Autowired
    private InteractionService interactionService;

    /**
     * 🔹 Obtenir l'historique des écoutes de l'utilisateur connecté
     */
    @GetMapping("/ecoutes")
    public ResponseEntity<?> getHistoriqueEcoutes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Non authentifié");
            return ResponseEntity.status(401).body(error);
        }
        
        String email = auth.getName();
        List<Ecoute> ecoutes = interactionService.getHistoriqueEcoutes(email);
        
        return ResponseEntity.ok(ecoutes);
    }

    /**
     * 🔹 Obtenir l'historique des téléchargements de l'utilisateur connecté
     */
    @GetMapping("/telechargements")
    public ResponseEntity<?> getHistoriqueTelechargements() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Non authentifié");
            return ResponseEntity.status(401).body(error);
        }
        
        String email = auth.getName();
        List<Telechargement> telechargements = interactionService.getHistoriqueTelechargements(email);
        
        return ResponseEntity.ok(telechargements);
    }
}
