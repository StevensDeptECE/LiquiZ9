package org.liquiz.stevens.controller;

import com.mongodb.client.MongoClient;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.lti.launch.controller.LtiLaunchController;
import edu.ksu.lti.launch.controller.OauthController;
import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.log4j.Logger;
import org.liquiz.stevens.exceptions.FileStorageException;
import org.liquiz.stevens.service.CanvasService;
import org.liquiz.stevens.service.FileService;
import org.liquiz.stevens.util.Assignment;
import org.liquiz.stevens.util.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.liquiz.stevens.mongodb.CodecQuizService;
import org.liquiz.stevens.mongodb.CodecQuizSubmissionService;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.liquiz.stevens.questions.Question;
import org.liquiz.stevens.quiz.Quiz;
import org.liquiz.stevens.quiz.QuizSubmission;

@Controller
@Scope("session")
public class LTILaunchExampleController extends LtiLaunchController{
    private static final Logger LOG = Logger.getLogger(LTILaunchExampleController.class);

    @Autowired
    public LtiSessionService ltiSessionService;

    protected Assignment assignment;

    @Autowired
    protected CanvasService canvasService;

    protected RoleChecker roleChecker;

    @Autowired
    FileService fileService;

    @Autowired
    MongoClient mongo;

    @Autowired
    CodecQuizService cqs;

    @Inject
    CodecQuizSubmissionService cqss;


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
        String dir = System.getProperty("user.dir");
        System.out.println(ltiSession.getEid());
        System.out.println(dir);
        return new ModelAndView("helloWorld", "username", ltiSession.getEid());
    }

    @RequestMapping("/chooseQuiz")
    public ModelAndView chooseQuiz(HttpServletRequest request) throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        //MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
        CodecQuizService cqs = new CodecQuizService();

        //ArrayList<Quiz> quizList = (ArrayList<Quiz>) cqs.getList(new Document("classId", ltiLaunchData.getCustom_canvas_course_id()));
        System.out.println(ltiSession.getEid());
        return new ModelAndView("chooseQuiz", "courseId", ltiLaunchData.getCustom_canvas_course_id());
    }

    @RequestMapping("/teacherView")
    public ModelAndView teacherView() throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        if(ltiSession.getEid() == null || ltiSession.getEid().isEmpty()){
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        //assertPrivledgedUser();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isInstructor = roleList !=null; //&& (roleList.contains(LtiLaunchData.InstitutionRole.Instructor));
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if(isInstructor){
            return new ModelAndView("teacherPage", "name", ltiLaunchData.getLis_person_name_family());
        }
        else{
            throw new AccessDeniedException("You are not an instructor and you cannot view this page");
        }
    }

    @RequestMapping("/editQuiz")
    public ModelAndView editQuiz(HttpServletRequest request, @RequestParam("quiz") String quizName) throws NoLtiSessionException, IOException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isInstructor = roleList !=null && (roleList.contains(LtiLaunchData.InstitutionRole.Instructor));
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        ModelAndView mav = new ModelAndView("editQuiz");
        mav.addObject("quiz", quiz);
        return mav;
    }

    @RequestMapping("/QuizRequest")
    public ModelAndView quizRequest(HttpServletRequest request, @RequestParam("quiz") String quizName) throws NoLtiSessionException, IOException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        return new ModelAndView("quizzes/" + quiz.getClassId() + "/" + quizName);
    }

    @RequestMapping("/showQuizzes")
    public ModelAndView showQuizzes(HttpServletRequest request) throws NoLtiSessionException, IOException {

        //canvasService.ensureApiTokenPresent();
        //canvasService.validateOauthToken();
        //assertPrivledgedUser();
        //List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        //boolean isInstructor = roleList !=null && (roleList.contains(LtiLaunchData.InstitutionRole.Instructor));
        //if(!isInstructor)
        //    throw new AccessDeniedException("You are not an insturctor and you cannot view this page");

        ArrayList<Quiz> quizList = (ArrayList<Quiz>) cqs.getList(new Document());
        ModelAndView mav = new ModelAndView("showQuizzes");
        mav.addObject("quizList", quizList);
        return mav;
    }

    @RequestMapping("/addQuiz")
    public ModelAndView addQuiz(HttpServletRequest request) throws NoLtiSessionException, IOException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        //canvasService.ensureApiTokenPresent();
        //canvasService.validateOauthToken();
        //assertPrivledgedUser();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        /*boolean isInstructor = roleList != null && (roleList.contains(LtiLaunchData.InstitutionRole.Instructor));
        if (!isInstructor)
            throw new AccessDeniedException("You are not an insturctor and you cannot view this page");
        */
        LOG.info(canvasService.getEid() + "is seeking to add a quiz to class " + ltiSession.getCanvasCourseId());
        ModelAndView mav = new ModelAndView("addQuiz");
        return mav;
    }

    @RequestMapping("/uploadQuiz")
    public ModelAndView uploadQuiz(HttpServletRequest request, @RequestParam("quizName") String quizName,
                                   @RequestParam("classId") String classId, @RequestParam("className") String className,
                                   @RequestParam("numTries") int numTries, @RequestParam("showAnswersAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showAnswersAfterLDT,
                                   @RequestParam("jsp File") MultipartFile jspFile, @RequestParam("Answer File") MultipartFile ansFile) throws NoLtiSessionException, RuntimeException {

        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();

        Date showAnswersAfter = Date.from(showAnswersAfterLDT.atZone(ZoneId.systemDefault()).toInstant());

        String ansFilePath = fileService.uploadAnswerFile(ansFile, classId).toString();
        fileService.uploadJspFile(jspFile, classId);
        LOG.info("uploading" +ansFile + "to:" + ansFilePath);
        String quizNameFile = ansFile.getName().replaceFirst("[.][^.]+$", "");
        Quiz quiz = new Quiz(quizName, ltiLaunchData.getCustom_canvas_course_id(), className, ansFilePath, numTries, showAnswersAfter);
        if(!cqs.exists(quiz.getQuizId()))
            cqs.add(quiz);
        else
            cqs.replaceQuiz(quiz);
        String success = "Your quiz " + quizNameFile + " was added successfully";

        ModelAndView mav = new ModelAndView("teacherPage", "name", ltiLaunchData.getLis_person_name_family());
        mav.addObject("success", success);
        return mav;
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
    
    @RequestMapping(value = "/getAnswers", method = RequestMethod.GET)
    public void getAnswers(HttpServletRequest request, HttpServletResponse response) throws NoLtiSessionException, IOException {
        canvasService.ensureApiTokenPresent();
        canvasService.validateOauthToken();

        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isStudent = roleList !=null && (roleList.contains(LtiLaunchData.InstitutionRole.Learner));
        
          PrintWriter out = response.getWriter();
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          response.addHeader("Access-Control-Allow-Origin", "*");
          CodecQuizService cqs = new CodecQuizService();
          CodecQuizSubmissionService cqss = new CodecQuizSubmissionService();
          
          String userId = ltiLaunchData.getCustom_canvas_user_id();
          String quizName = "demo";
          
          Quiz quiz = cqs.getOne(new Document("quizName", quizName));
          QuizSubmission quizSubmission= cqss.getOne(new Document("quizName", quizName).append("userName", userId));
          //TODO: throw denied access if not in class
          JSONArray jArr = new JSONArray();
          JSONObject jObj;
          
          if(quiz!=null && quizSubmission!=null && new Date().after(quiz.getAnswersRelease())){
            double[] questionGradesArr = quizSubmission.getQuestionGrades();
            TreeMap<Integer,Question> questionsMap = quiz.getQuestionsMap();
            
            Iterator<Map.Entry<Integer, Question>> questionsItr = questionsMap.entrySet().iterator();
            int index = 0;
            
            while(questionsItr.hasNext() && index < questionGradesArr.length){
                Map.Entry<Integer, Question> qEntry = questionsItr.next();
                
                Question q = qEntry.getValue();
                jObj = new JSONObject();
                jObj.put("id", q.getName());
                jObj.put("pointsT", q.getGradeValue());
                jObj.put("pointsE", questionGradesArr[index++]);
                jObj.put("answers", q.getAnswer());
                jArr.add(jObj);
            }
          }
          
        out.print(jArr);
        out.flush();
          
    }

    @RequestMapping(value = {"/quizResult", "/quizResult/{id:[\\d]+}"})
    public ModelAndView quizResult(HttpServletRequest request, @PathVariable("id") long id) throws NoLtiSessionException, IOException {
        //canvasService.ensureApiTokenPresent();
        //canvasService.validateOauthToken();
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        Quiz quiz = cqs.getOne(new Document("quizId", id));
        if(quiz==null)
            throw new RuntimeException("quiz does not exist");
        HttpSession session=request.getSession();
        session.setAttribute("quizName", quiz.getQuizName());
        /*List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isStudent = roleList !=null && (roleList.contains(LtiLaunchData.InstitutionRole.Learner));
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if(!isStudent && ltiLaunchData.getCustom_canvas_course_id()!=quiz.getClassId())
            throw new AccessDeniedException("You cannot access this quiz if you are not a student within the correct course");
         */
        return new ModelAndView("quizzes/" + quiz.getClassId() + "/" + quiz.getQuizName());
    }

    @RequestMapping(value = "/quizChoice", method = RequestMethod.POST)
    public ModelAndView quizChoice(HttpServletRequest request) throws NoLtiSessionException, IOException {
        String filename = request.getParameter("quiz");
        String name = filename.replaceFirst("[.][^.]+$", "");
        HttpSession session=request.getSession();
        session.setAttribute("quizName", name);
        return new ModelAndView("quizzes/" + name);
    }
    
    @RequestMapping(value = "/testQuiz", method = RequestMethod.POST)
    public ModelAndView testQuiz(HttpServletRequest request) throws NoLtiSessionException, IOException {
        //canvasService.ensureApiTokenPresent();
        //canvasService.validateOauthToken();
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();

        String userId = ltiLaunchData.getCustom_canvas_user_login_id();
        
        Map<String, String[]> paramsMap = request.getParameterMap();
          TreeMap<String, String[]> inputsMap = new TreeMap<>();
          if(inputsMap.containsKey("pledged"))
              inputsMap.putAll(paramsMap);
          inputsMap.remove("pledged");
         
          HttpSession session=request.getSession(true);
          String quizName=(String)session.getAttribute("quizName");
          Quiz quiz = cqs.getOne(new Document("quizName", quizName));
          if(quiz == null)
            throw new RuntimeException("quiz does not exist");
          QuizSubmission quizSub = new QuizSubmission(quizName, ltiLaunchData.getCustom_canvas_user_login_id(), inputsMap, quiz);
          String outcome = "your submission was added successfully";
          if(quiz.getNumTries() > cqss.getTries(new Document("quizName", quizName).append("userId", userId))){
            if(quizSub!=null) {
              cqss.add(quizSub);
            }
            else
              outcome = "Submission failed";
          }
          else
              outcome = "no more tries are allowed so this submission did not count";
          ModelAndView mav = new ModelAndView("viewGrade", "success", outcome);
          mav.addObject("username", userId);
          mav.addObject("grade", quizSub.getGrade());

          return mav;
    }

    @RequestMapping(value = "/test")
    public ModelAndView test() throws NoLtiSessionException, RuntimeException {
        if(true)
            throw new RuntimeException(System.getProperty("user.dir"));
        return new ModelAndView("teacherView");
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
        return "/teacherView";
    }

    @Override
    protected String getApplicationName() {
        return "Liquiz_test2";
    }
}
