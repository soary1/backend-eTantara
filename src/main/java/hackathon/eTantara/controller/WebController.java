package hackathon.eTantara.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    /**
     * Page d'accueil avec redirection vers dashboard
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    /**
     * Page de connexion
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Vous avez été déconnecté avec succès");
        }
        return "login";
    }

    /**
     * Page d'inscription
     */
    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "error", required = false) String error,
                              Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Erreur lors de l'inscription");
        }
        return "register";
    }

    /**
     * Page du tableau de bord (après connexion)
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }
    
    /**
     * Page des contenus culturels
     */
    @GetMapping("/contenus")
    public String contenus(Model model) {
        return "contenus";
    }
}
