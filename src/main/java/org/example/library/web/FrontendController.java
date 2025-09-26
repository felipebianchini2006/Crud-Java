package org.example.library.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Controller para servir o frontend React SPA
 */
@RestController
public class FrontendController {

    @GetMapping(value = {"/app", "/app/**"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> serveApp() throws IOException {
        Resource resource = new ClassPathResource("static/app/index.html");
        if (resource.exists()) {
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String redirectToApp() {
        return "redirect:/app";
    }
}
