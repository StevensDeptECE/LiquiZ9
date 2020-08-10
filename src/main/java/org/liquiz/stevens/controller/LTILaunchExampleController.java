package org.liquiz.stevens.controller;

import com.mongodb.client.MongoClient;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.lti.launch.controller.LtiLaunchController;
import edu.ksu.lti.launch.controller.OauthController;
import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.apache.log4j.Logger;
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
public class LTILaunchExampleController extends LtiLaunchController {
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

    @Autowired
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

        //ArrayList<Quiz> quizList = (ArrayList<Quiz>) cqs.getList(new Document("classId", ltiLaunchData.getCustom_canvas_course_id()));
        System.out.println(ltiSession.getEid());
        return new ModelAndView("chooseQuiz", "courseId", ltiLaunchData.getCustom_canvas_course_id());
    }

    /**
     * Initial page for professors where they can choose to view/edit their quizzes, add a new quiz or view grades
     *
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping("/teacherView")
    public ModelAndView teacherView() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        ModelAndView mav = new ModelAndView("teacherPage", "name", ltiLaunchData.getLis_person_name_family());
        mav.addObject("file", System.getProperty("user.dir"));
        return mav;
    }

    @RequestMapping("/QuizzesToEdit")
    public ModelAndView quizzesToEdit() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession()
        String courseId = ltiLaunchData.getCustom_canvas_course_id();
        ArrayList<Quiz> quizList = cqs.getList(new Document("classId", courseId));
        ModelAndView mav = new ModelAndView("quizzesToEdit");
        mav.addObject("quizList", quizList);
        return mav;
    }

    @RequestMapping("/editQuiz")
    public ModelAndView editQuiz(HttpServletRequest request, @RequestParam("quiz") String quizName) throws NoLtiSessionException, IOException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if (!isIntructor())
            throw new AccessDeniedException("You are not an insturctor and you cannot view this page");
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        ModelAndView mav = new ModelAndView("editQuiz");
        mav.addObject("quiz", quiz);
        return mav;
    }

    @RequestMapping("/QuizRequest")
    public ModelAndView quizRequest(HttpServletRequest request, @RequestParam("quiz") String quizIdString) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        long quizId = Long.parseLong(quizIdString);
        /*if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }*/
        Quiz quiz = cqs.getOne(new Document("quizId", quizId));
        HttpSession session = request.getSession();
        session.setAttribute("quizName", quiz.getQuizName());
        //TODO: change class to course
        return new ModelAndView("quizzes/" + quiz.getCourseId() + "/" + quiz.getQuizName());
    }

    @RequestMapping("/showQuizzes")
    public ModelAndView showQuizzes(HttpServletRequest request) throws NoLtiSessionException, IOException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        ArrayList<Quiz> quizList = cqs.getList(new Document());
        ModelAndView mav = new ModelAndView("showQuizzes");
        mav.addObject("quizList", quizList);
        return mav;
    }

    @RequestMapping("/addQuiz")
    public ModelAndView addQuiz(HttpServletRequest request) throws NoLtiSessionException, IOException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        LOG.info(canvasService.getEid() + "is seeking to add a quiz to class " + ltiSession.getCanvasCourseId());
        ModelAndView mav = new ModelAndView("addQuiz");
        return mav;
    }

    @RequestMapping("/uploadQuiz")
    public ModelAndView uploadQuiz(HttpServletRequest request, @RequestParam("quizName") String quizName,
                                   @RequestParam("classId") String classId, @RequestParam("className") String className,
                                   @RequestParam("numTries") int numTries, @RequestParam("showAnswersAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showAnswersAfterLDT,
                                   @RequestParam("jsp File") MultipartFile jspFile, @RequestParam("Answer File") MultipartFile ansFile) throws NoLtiSessionException, RuntimeException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        Date showAnswersAfter = Date.from(showAnswersAfterLDT.atZone(ZoneId.systemDefault()).toInstant());

        fileService.uploadJspFile(jspFile, classId);
        String ansFilePath = fileService.uploadAnswerFile(ansFile, classId).toString();


        LOG.info("uploading" + ansFile + "to:" + ansFilePath);
        String quizNameFile = ansFile.getName().replaceFirst("[.][^.]+$", "");
        Quiz quiz = new Quiz(quizNameFile, ltiLaunchData.getCustom_canvas_course_id(), className, ansFilePath, numTries, showAnswersAfter);
        if (!cqs.exists(quiz.getQuizId())) {
            cqs.add(quiz);
            LOG.info(quiz.getQuizName() + "(" + quiz.getQuizId() + ") has been added to the mongo database");
        } else {
            cqs.replaceQuiz(quiz);
            LOG.info(quiz.getQuizName() + "(" + quiz.getQuizId() + ") has been replaced in the mongo database");
        }
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
        if (outcome == null)
            throw new RuntimeException("outcome is null");
        assignment.setLis_outcome_service_url(outcome);
        String sourcedid = request.getParameter("lis_result_sourcedid");
        if (sourcedid == null)
            throw new RuntimeException("sourcedid is null");
        assignment.setLis_result_sourcedid(sourcedid);

        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        boolean isStudent = roleList != null && (roleList.contains(LtiLaunchData.InstitutionRole.Learner));
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
        boolean isStudent = roleList != null && (roleList.contains(LtiLaunchData.InstitutionRole.Learner));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        CodecQuizService cqs = new CodecQuizService();
        CodecQuizSubmissionService cqss = new CodecQuizSubmissionService();

        String userId = ltiLaunchData.getCustom_canvas_user_id();
        String quizName = "demo";

        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        QuizSubmission quizSubmission = cqss.getOne(new Document("quizName", quizName).append("userName", userId));
        //TODO: throw denied access if not in class
        JSONArray jArr = new JSONArray();
        JSONObject jObj;

        if (quiz != null && quizSubmission != null && new Date().after(quiz.getAnswersRelease())) {
            double[] questionGradesArr = quizSubmission.getQuestionGrades();
            TreeMap<Integer, Question> questionsMap = quiz.getQuestionsMap();

            Iterator<Map.Entry<Integer, Question>> questionsItr = questionsMap.entrySet().iterator();
            int index = 0;

            while (questionsItr.hasNext() && index < questionGradesArr.length) {
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
        LtiLaunchData ltiLaunchData = getStudentSession();
        Quiz quiz = cqs.getOne(new Document("quizId", id));
        if (quiz == null)
            throw new RuntimeException("quiz does not exist");
        HttpSession session = request.getSession();
        session.setAttribute("quizName", quiz.getQuizName());
        return new ModelAndView("quizzes/" + quiz.getCourseId() + "/" + quiz.getQuizName());
    }


    @RequestMapping(value = "/submitQuiz", method = RequestMethod.POST)
    public ModelAndView testQuiz(HttpServletRequest request) throws NoLtiSessionException, IOException {
        LtiLaunchData ltiLaunchData = getStudentSession();

        String userId = ltiLaunchData.getCustom_canvas_user_login_id();

        Map<String, String[]> paramsMap = request.getParameterMap();
        TreeMap<String, String[]> inputsMap = new TreeMap<>();
        inputsMap.putAll(paramsMap);
        if (inputsMap.containsKey("pledged"))
            inputsMap.remove("pledged");

        HttpSession session = request.getSession(true);
        //use real quiz id
        String quizName = (String) session.getAttribute("quizName");
        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        if (quiz == null)
            throw new RuntimeException("quiz does not exist");
        QuizSubmission quizSub = new QuizSubmission(quizName, ltiLaunchData.getCustom_canvas_user_login_id(), inputsMap, quiz);
        String outcome = "your submission was added successfully";
        if (quiz.getNumTries() > cqss.getTries(new Document("quizName", quizName).append("userId", userId))) {
            if (quizSub != null) {
                cqss.add(quizSub);
            } else
                outcome = "Submission failed";
        } else
            outcome = "no more tries are allowed so this submission did not count";
        ModelAndView mav = new ModelAndView("viewGrade", "success", outcome);
        mav.addObject("username", userId);
        mav.addObject("grade", quizSub.getGrade());
        mav.addObject("maxGrade", quiz.getMaxGrade());
        mav.addObject("qInputs", quizSub.getInputs());
        mav.addObject("qGrades", quizSub.getQuestionGrades());
        mav.addObject("qMaxGrades", quiz.getMaxGrades());

        return mav;
    }

    @RequestMapping(value = "/test")
    public ModelAndView test() throws NoLtiSessionException, RuntimeException {
        if (true)
            throw new RuntimeException(System.getProperty("user.dir"));
        return new ModelAndView("teacherView");
    }


    private void assertPrivledgedUser() throws NoLtiSessionException {
        if (canvasService.getEid() == null || canvasService.getEid().isEmpty()) {
            throw new NoLtiSessionException();
        }
        List<LtiLaunchData.InstitutionRole> roles = canvasService.getRoles();
        if (!roleChecker.roleAllowed(roles)) {
            LOG.error("User (" + canvasService.getEid() + ") doesn't have privledge to launch" + roles);
            throw new NoLtiSessionException();
        }


    }

    private MultipleSubmissionsOptions.StudentSubmissionOption generateStudentSubmissionsOptions(MultipleSubmissionsOptions submissionsOptions) {
        Double grade = 100.0;
        return submissionsOptions.createStudentSubmissionOption(null, grade.toString(), false, false, null, null);
    }

    private LtiLaunchData accessCheck() throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if(ltiSession.getEid()==null||ltiSession.getEid().isEmpty())
        {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        return ltiLaunchData;
    }

    private LtiLaunchData getTeacherSession() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = accessCheck();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        if(roleList != null && (roleList.contains(LtiLaunchData.InstitutionRole.Instructor)))
        {
            throw new AccessDeniedException("You are not an instructor and you cannot view this page");
        }
        return ltiLaunchData;
    }

    private LtiLaunchData getStudentSession() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = accessCheck();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        if(roleList != null && (roleList.contains(LtiLaunchData.InstitutionRole.Student)))
        {
            throw new AccessDeniedException("You are not an instructor and you cannot view this page");
        }
        return ltiLaunchData;
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
