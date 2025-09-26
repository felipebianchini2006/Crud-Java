package org.example.library.web;

import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MeController {

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        // Se não há autenticação, retorna null indicando que não há usuário logado
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.ok(Map.of("user", (Object) null));
        }
        
        // Se há autenticação, retorna os dados do usuário
        try {
            CustomUserDetailsService.CustomUserPrincipal principal =
                    (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            User u = principal.getUser();
            return ResponseEntity.ok(Map.of(
                    "user", Map.of(
                            "id", u.getId(),
                            "name", u.getName(),
                            "email", u.getEmail(),
                            "role", u.getRole(),
                            "profileImage", u.getProfileImage()
                    )
            ));
        } catch (Exception e) {
            // Em caso de erro ao obter dados do usuário, retorna que não há usuário
            return ResponseEntity.ok(Map.of("user", (Object) null));
        }
    }
}

