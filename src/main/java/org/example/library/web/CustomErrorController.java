package org.example.library.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        model.addAttribute("status", status);
        model.addAttribute("exception", exception);
        model.addAttribute("message", message);
        model.addAttribute("requestUri", requestUri);

        // Log the error for debugging
        System.err.println("Error occurred:");
        System.err.println("Status: " + status);
        System.err.println("URI: " + requestUri);
        System.err.println("Message: " + message);
        if (exception != null) {
            System.err.println("Exception: " + exception.toString());
            if (exception instanceof Exception) {
                ((Exception) exception).printStackTrace();
            }
        }

        return "error";
    }
}
