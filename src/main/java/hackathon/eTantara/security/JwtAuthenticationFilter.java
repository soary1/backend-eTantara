package hackathon.eTantara.security;

import hackathon.eTantara.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 1. Si la route est publique -> ne rien vérifier
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 2. Lire le header Authorization (peut être absent)
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);

            try {
                String email = jwtService.extractEmail(jwt);

                if (email != null && jwtService.validateToken(jwt, email)) {
                    // ✅ Authentification réussie
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.emptyList()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ Token valide pour : " + email);
                } else {
                    System.out.println("⚠️ Token invalide ou expiré");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Erreur de parsing JWT : " + e.getMessage());
            }
        } else {
            // Aucun token fourni → on laisse passer
            System.out.println("ℹ️ Aucun token JWT dans la requête : " + path);
        }

        // 🔹 3. Continuer la chaîne de filtres sans bloquer
        filterChain.doFilter(request, response);
    }

    /**
     * ✅ Routes publiques à ne jamais protéger
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/api/test")
                || path.startsWith("/api/contenus") && (path.contains("/audio") || path.contains("/telecharger"))
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/")
                || path.equals("/login")
                || path.equals("/register")
                || path.equals("/contenus")
                || path.startsWith("/h2-console");
    }
}
