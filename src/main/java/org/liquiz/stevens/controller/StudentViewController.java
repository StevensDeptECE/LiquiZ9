package org.liquiz.stevens.controller;

import edu.ksu.lti.launch.controller.LtiLaunchController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/student")
@Controller
@Scope("session")
public class StudentViewController extends LtiLaunchController {
    @Override
    protected String getInitialViewPath() {
        return "/quizResult";
    }

    @Override
    protected String getApplicationName() {
        return "LiquiZ";
    }
}
