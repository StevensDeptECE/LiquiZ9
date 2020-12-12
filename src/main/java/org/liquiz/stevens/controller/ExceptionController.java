package org.liquiz.stevens.controller;

import edu.ksu.canvas.exception.InvalidOauthTokenException;
import edu.ksu.lti.launch.exception.OauthTokenRequiredException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(OauthTokenRequiredException.class)
    public String initiateOauthToken(OauthTokenRequiredException e) {
        return "redirect:/beginOauth";
    }

    @ExceptionHandler(InvalidOauthTokenException.class)
    public String initiateOauthToken(InvalidOauthTokenException e) {
        return "redirect:/beginOauth";
    }
}
