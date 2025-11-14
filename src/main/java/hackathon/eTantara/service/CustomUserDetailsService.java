package hackathon.eTantara.service;

import hackathon.eTantara.entite.Utilisateur;
import hackathon.eTantara.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(usernameOrEmail)
                .orElse(utilisateurRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + usernameOrEmail)));
        
        return User.builder()
                .username(utilisateur.getEmail()) // Toujours utiliser l'email comme identifiant principal
                .password(utilisateur.getMotDePasse())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole())))
                .build();
    }
}
