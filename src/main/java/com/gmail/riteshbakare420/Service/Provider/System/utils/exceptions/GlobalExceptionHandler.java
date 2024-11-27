package com.gmail.riteshbakare420.Service.Provider.System.utils.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("An unexpected error occurred", e);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error"; // Create a generic error.html template
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        logger.warn("Access denied", e);
        return "redirect:/access-denied";
    }
}
