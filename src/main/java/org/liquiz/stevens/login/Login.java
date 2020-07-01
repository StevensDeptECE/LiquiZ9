package org.liquiz.stevens.login;

import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class Login {

    @Autowired
    public LtiSessionService ltiSessionService;

    public static void checkLogin() {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        if(ltiSession==null){

        }
    }
}
