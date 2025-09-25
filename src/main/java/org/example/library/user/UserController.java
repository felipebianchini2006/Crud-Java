package org.example.library.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.example.library.security.CustomUserDetailsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return new UserResponse(user);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<User> users = userService.findAll();
        
        Map<String, Object> stats = Map.of(
            "total", users.size(),
            "readers", userService.findByRole(UserRole.READER).size(),
            "authors", userService.findByRole(UserRole.AUTHOR).size(),
            "admins", userService.findByRole(UserRole.ADMIN).size()
        );
        
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        User user = userService.create(request);
        return new UserResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.update(id, request);
        return new UserResponse(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Upload de foto de perfil
    @PostMapping("/{id}/upload-image")
    public UserResponse uploadImage(@PathVariable Long id,
                                    @RequestParam("file") MultipartFile file) throws java.io.IOException {
        String url = userService.uploadProfileImage(id, file);
        User user = userService.findById(id);
        return new UserResponse(user);
    }

    // Ativar/Desativar usuário (admin)
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
