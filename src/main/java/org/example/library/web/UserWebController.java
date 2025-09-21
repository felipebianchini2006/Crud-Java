package org.example.library.web;

import org.example.library.security.CustomUserDetailsService;
import org.example.library.user.User;
import org.example.library.user.UserService;
import org.example.library.user.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class UserWebController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String profile(Authentication authentication, Model model) {
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        
        model.addAttribute("user", user);
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute UserUpdateRequest userUpdateRequest,
                               BindingResult result,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/profile";
        }

        try {
            userService.update(user.getId(), userUpdateRequest);
            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "user/profile";
        }
    }

    @PostMapping("/upload-image")
    public String uploadProfileImage(@RequestParam("file") MultipartFile file,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        
        CustomUserDetailsService.CustomUserPrincipal principal = 
            (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Selecione uma imagem para upload.");
            return "redirect:/profile";
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("error", "Apenas arquivos de imagem são permitidos.");
            return "redirect:/profile";
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("error", "A imagem deve ter no máximo 5MB.");
            return "redirect:/profile";
        }

        try {
            userService.uploadProfileImage(user.getId(), file);
            redirectAttributes.addFlashAttribute("success", "Foto de perfil atualizada com sucesso!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao fazer upload da imagem: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}
