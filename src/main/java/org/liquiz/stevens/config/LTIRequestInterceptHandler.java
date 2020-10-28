package org.liquiz.stevens.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.liquiz.stevens.service.CanvasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LTIRequestInterceptHandler extends HandlerInterceptorAdapter{

    @Autowired
    public LtiSessionService ltiSessionService;

    @Autowired
    protected CanvasService canvasService;

    private static final Logger logger = LoggerFactory
            .getLogger(LTIRequestInterceptHandler.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception, NoLtiSessionException {
        logger.info("Request URL::" + request.getRequestURL().toString()
                + ":: Start Time=" + System.currentTimeMillis());
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        canvasService.ensureApiTokenPresent();
        canvasService.validateOauthToken();
        if(ltiSession.getEid() == null || ltiSession.getEid().isEmpty()){
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        return true;
    }
}
