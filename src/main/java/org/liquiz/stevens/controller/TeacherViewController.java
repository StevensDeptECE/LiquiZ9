package org.liquiz.stevens.controller;

import edu.ksu.lti.launch.controller.LtiLaunchController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/teacher")
@Controller
@Scope("session")
public class TeacherViewController extends LtiLaunchController {
    @Override
    protected String getInitialViewPath() {
        return "/teacherView";
    }

    @Override
    protected String getApplicationName() {
        return "LiquiZ";
    }
}
