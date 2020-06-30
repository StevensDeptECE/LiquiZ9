package org.liquiz.stevens.controller;

import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.lti.launch.controller.LtiLaunchController;
import edu.ksu.lti.launch.controller.OauthController;
import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.apache.log4j.Logger;
import org.liquiz.stevens.service.CanvasService;
import org.liquiz.stevens.util.Assignment;
import org.liquiz.stevens.util.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
@Scope("session")
public class LTILaunchExampleController extends LtiLaunchController{
    private static final Logger LOG = Logger.getLogger(LTILaunchExampleController.class);

    public LtiSessionService ltiSessionService;

    protected Assignment assignment;

    protected CanvasService canvasService;

    protected RoleChecker roleChecker;

    public LTILaunchExampleController(LtiSessionService ltiSessionService, CanvasService canvasService){
        this.ltiSessionService = ltiSessionService;
        this.canvasService = canvasService;
    }
    /**
     * We have our applications return the LTI configuration XML when you hit
     * the root of the context in a browser. It's an easy place to keep
     * the necessary XML and this method sets the host name/port to appropriate
     * values when running in dev/test by examining the incoming HTTP request.
     */
    @RequestMapping("/")
    public ModelAndView basePath(HttpServletRequest request) {
        LOG.info("Showing Activity Reporting configuration XML");
        String ltiLaunchUrl = OauthController.getApplicationBaseUrl(request, true) + "/launch";
        LOG.debug("LTI launch URL: " + ltiLaunchUrl);
        return new ModelAndView("ltiConfigure", "url", ltiLaunchUrl);
    }

    @RequestMapping("/helloWorld")
    public ModelAndView showButton() throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        System.out.println(ltiSession.getEid());
        return new ModelAndView("grade", "username", ltiSession.getEid());
    }

    @RequestMapping(value = "/grade", method = RequestMethod.POST)
    public void grade(HttpServletRequest request) throws NoLtiSessionException, IOException {

        canvasService.ensureApiTokenPresent();
        canvasService.validateOauthToken();

        String outcome = request.getParameter("lis_outcome_service_url");
        if( outcome == null)
            throw new RuntimeException("outcome is null");
        assignment.setLis_outcome_service_url(outcome);
        String sourcedid = request.getParameter("lis_result_sourcedid");
        if(sourcedid == null)
            throw new RuntimeException("sourcedid is null");
        assignment.setLis_result_sourcedid(sourcedid);

        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isStudent = roleList !=null && (roleList.contains(LtiLaunchData.InstitutionRole.Learner));
        LOG.info(canvasService.getEid() + "is accessing this endpoint");

        assertPrivledgedUser();
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> studentMap;
        MultipleSubmissionsOptions submissionsOptions = new MultipleSubmissionsOptions(ltiSession.getCanvasCourseId().toString(), Integer.parseInt(assignment.getLis_result_sourcedid()), null);
        //return new ModelAndView("grade/" + ltiSession.getCanvasCourseId().toString() + "/" + ltiSession.getEid().toString());
    }

    private void assertPrivledgedUser() throws NoLtiSessionException {
        if(canvasService.getEid() == null || canvasService.getEid().isEmpty()){
            throw new NoLtiSessionException();
        }
        List<LtiLaunchData.InstitutionRole> roles = canvasService.getRoles();
        if(!roleChecker.roleAllowed(roles)){
            LOG.error("User (" + canvasService.getEid() + ") doesn't have privledge to launch" + roles);
            throw new NoLtiSessionException();
        }


    }

    private MultipleSubmissionsOptions.StudentSubmissionOption generateStudentSubmissionsOptions(MultipleSubmissionsOptions submissionsOptions){
        Double grade = 100.0;
        return submissionsOptions.createStudentSubmissionOption(null, grade.toString(), false, false, null, null);
    }

    /**
     * After authenticating the LTI launch request, the user is forwarded to
     * this path. It is the initial page your user will see in their browser.
     */
    @Override
    protected String getInitialViewPath() {
        return "/helloWorld";
    }

    @Override
    protected String getApplicationName() {
        return "Liquiz_test2";
    }
}
