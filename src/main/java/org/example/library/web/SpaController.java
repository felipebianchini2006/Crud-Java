package org.example.library.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    // Forward only paths WITHOUT a dot to index.html
    @GetMapping({
            "/app",
            "/app/{path:[^\\.]*}",
            "/app/**/{path:[^\\.]*}"
    })
    public String forwardSpa() {
        return "forward:/static/app/index.html";
    }
}

