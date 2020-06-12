package org.liquiz.stevens.controller;

import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.lti.launch.controller.LtiLaunchController;

import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import org.apache.log4j.Logger;
import org.liquiz.stevens.service.CanvasService;
import org.liquiz.stevens.util.RoleChecker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

//https://github.com/kstateome/lti-attendance/blob/374acac3aefbfe89fd148c5e353e19e422f03e1c/src/main/java/edu/ksu/canvas/attendance/controller/AttendanceBaseController.java
@Controller
@Scope("session")
public class GradeController extends LtiLaunchController {
    private static final Logger LOG = Logger.getLogger(GradeController.class);

    protected CanvasService canvasService;

    protected RoleChecker roleChecker;

    @Override
    protected String getInitialViewPath() {
        return "/grade";
    }

    @Override
    protected String getApplicationName() {
        return "Grade";
    }

    @RequestMapping("/grade")
    public ModelAndView grade() throws NoLtiSessionException, IOException {
        canvasService.ensureApiTokenPresent();
        canvasService.validateOauthToken();

        assertPrivledgedUser();
        return new ModelAndView("grade");
    }

    private void assertPrivledgedUser() throws NoLtiSessionException {
        if(canvasService.getEid() == null || canvasService.getEid().isEmpty()){
            //throw runtime exception
        }
        List<LtiLaunchData.InstitutionRole> roles = canvasService.getRoles();
        if(!roleChecker.roleAllowed(roles)){
            LOG.error("User (" + canvasService.getEid() + ") doesn't have privledge to launch" + roles);
            //throw new runtime exception
        }


    }

    private MultipleSubmissionsOptions.StudentSubmissionOption generateStudentSubmissionsOptions(MultipleSubmissionsOptions submissionsOptions){
        Double grade = 100.0;
        return submissionsOptions.createStudentSubmissionOption(null, grade.toString(), false, false, null, null);
    }
    //line 167 in Assignmentsubmitter.java
    //return submissionOptions.createStudentSubmissionOption(comments.toString(), grade.toString(), null, null, null, null);


}
