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
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).build();
        }
        CustomUserDetailsService.CustomUserPrincipal principal =
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User u = principal.getUser();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "name", u.getName(),
                "email", u.getEmail(),
                "role", u.getRole(),
                "profileImage", u.getProfileImage()
        ));
    }
}

