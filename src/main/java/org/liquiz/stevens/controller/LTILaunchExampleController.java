package org.liquiz.stevens.controller;

import com.mongodb.client.MongoClient;
import com.sun.org.apache.xpath.internal.operations.Mod;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.lti.launch.controller.LtiLaunchController;
import edu.ksu.lti.launch.controller.OauthController;
import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.liquiz.stevens.quiz.qNameComparator;
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
        return mav;
    }

    /**
     * Provides a list of the quizzes a professor can edit,delete or view from their course
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping("/quizzesToEdit")
    public ModelAndView quizzesToEdit() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        String courseId = ltiLaunchData.getCustom_canvas_course_id();
        ArrayList<Quiz> quizList = cqs.getList(new Document("classId", courseId));
        HashMap<Long, Double> avgGrades = new HashMap<Long, Double>();
        for(Quiz q : quizList)
            avgGrades.put(q.getQuizId(), cqss.getAvgQuizScore(q.getQuizId()));
        ModelAndView mav = new ModelAndView("quizzesToEdit");
        mav.addObject("quizList", quizList);
        mav.addObject("avgGrades", avgGrades);
        return mav;
    }

    /**
     * Returns a page where a teacher can edit their selected quiz
     * @param id
     * @param request
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/editQuiz", "/editQuiz{id:[\\d]+}"})
    public ModelAndView editQuiz(@PathVariable("id") long id,HttpServletRequest request) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        Quiz quiz = cqs.getOne(new Document("quizId", id));

        ModelAndView mav = new ModelAndView("editQuiz", "quiz", quiz);
        return mav;
    }

    /**
     * Deletes the selected quizzes and returns the quizzesToEdit page without those deleted quizzes
     * @param request
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping("/deleteQuizzes")
    public ModelAndView deleteQuizzes(HttpServletRequest request) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        String courseId = ltiLaunchData.getCustom_canvas_course_id();

        String[] quizDeleteArray = request.getParameterValues("quiz");

        String Outcome = "The quizzes with the following IDs have been deleted: ";
        for(String quizIdString : quizDeleteArray){
            long quizId = Long.parseLong(quizIdString);
            if(cqs.delete(quizId))
                Outcome += "(" + quizIdString + ") ";
        }


        ArrayList<Quiz> quizList = cqs.getList(new Document("classId", courseId));
        double[] avgGrades = new double[quizList.size()];
        int index = 0;
        for(Quiz q : quizList)
            avgGrades[index++] = cqss.getAvgQuizScore(q.getQuizId());

        ModelAndView mav = new ModelAndView("quizzesToEdit", "Outcome", Outcome);
        mav.addObject("quizList", quizList);
        mav.addObject("avgGrades", avgGrades);
        return mav;
    }

    /**
     * Deletes the selected submissions and returns to the viewSubmissions page with the new number of submissions
     * @param id identifies the quiz for which the submissions are returned
     * @param request
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/deleteSubmissions", "/deleteSubmissions{id:[\\d]+}"})
    public ModelAndView deleteSubmissions(@PathVariable("id") long id, HttpServletRequest request) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        String[] quizSubmissionDeleteArray = request.getParameterValues("submission");

        String Outcome = "The quiz submissions with the following ids have been deleted: ";

        for(String quizSubmissionIdString : quizSubmissionDeleteArray){
            if(cqss.delete(quizSubmissionIdString))
                Outcome += "(" + quizSubmissionIdString + ") ";
        }

        ArrayList<QuizSubmission> quizSubList = cqss.getList(new Document("quizId", id));
        Quiz quiz = cqs.getOne(new Document("quizId", id));

        ModelAndView mav = new ModelAndView("viewSubmissions", "quizSubList", quizSubList);
        mav.addObject("quizName", quiz.getQuizName());
        mav.addObject("quizId", quiz.getQuizId());
        return mav;
    }

    /**
     * Returns a list of quizsubmissions which the user can choose to view
     * @param
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/viewSubmissions", "/viewSubmissions{id:[\\d]+}"})
    public ModelAndView viewSubmissions( @PathVariable("id") long id) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        ArrayList<QuizSubmission> quizSubList = cqss.getList(new Document("quizId", id));
        Quiz quiz = cqs.getOne(new Document("quizId", id));

        ModelAndView mav = new ModelAndView("viewSubmissions", "quizSubList", quizSubList);
        mav.addObject("quizName", quiz.getQuizName());
        mav.addObject("quizId", quiz.getQuizId());
        return mav;
    }

    /**
     * returns a spreadsheet with the information from the submissions for the given quiz matching the id
     * @param id
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/createSpreadsheetQuiz", "/createSpreadsheetQuiz{id:[\\d]+}"})
    public ModelAndView createSpreadsheetQuiz( @PathVariable("id") long id) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        ArrayList<QuizSubmission> quizSubList = cqss.getList(new Document("quizId", id));
        Quiz quiz = cqs.getOne(new Document("quizId", id));

        ModelAndView mav = new ModelAndView("createSpreadsheetQuiz", "quizSubList", quizSubList);
        mav.addObject("quiz", quiz);
        return mav;
    }

    /**
     * returns a spreadsheet with the information from a submission that matches the given id
     * @param id
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/createSpreadsheetSubmission", "/createSpreadsheetSubmission{id:[A-Za-z0-9]+}"})
    public ModelAndView createSpreadsheetSubmission( @PathVariable("id") String id) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        ObjectId objID = new ObjectId(id);

        QuizSubmission quizSub = cqss.getOne(new Document("_id", objID));
        Quiz quiz = cqs.getOne(new Document("quizId", quizSub.getQuizId()));
        String[] quizAnswers = quiz.getAnswers();

        ModelAndView mav = new ModelAndView("createSpreadsheetSubmission", "quizSub", quizSub);
        mav.addObject("quizAnswers", quizAnswers);

        return mav;
    }

    /**
     * Page where detailed editing occurs for a selected quiz
     * @param request
     * @param quizName
     * @return
     * @throws NoLtiSessionException
     * @throws IOException
     */
    @RequestMapping("/editQuiz")
    public ModelAndView editQuiz(HttpServletRequest request, @RequestParam("quiz") String quizName) throws NoLtiSessionException, IOException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        Quiz quiz = cqs.getOne(new Document("quizName", quizName));
        ModelAndView mav = new ModelAndView("editQuiz");
        mav.addObject("quiz", quiz);
        return mav;
    }

    /**
     * Provides the quiz to a professor when they choose to view one
     * @param request
     * @param quizIdString
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping("/QuizRequest")
    public ModelAndView quizRequest(HttpServletRequest request, @RequestParam("quiz") String quizIdString) throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        long quizId = Long.parseLong(quizIdString);
        Quiz quiz = cqs.getOne(new Document("quizId", quizId));
        HttpSession session = request.getSession();
        session.setAttribute("quizName", quiz.getQuizName());
        return new ModelAndView("quizzes/" + quiz.getCourseId() + "/" + quiz.getQuizName());
    }

    /**
     * Page where parameters to add a quiz and the set files are filled out and uploaded
     * @param
     * @return
     * @throws NoLtiSessionException
     * @throws IOException
     */
    @RequestMapping("/addQuiz")
    public ModelAndView addQuiz() throws NoLtiSessionException, IOException {
        LtiLaunchData ltiLaunchData = getTeacherSession();
        LOG.info(canvasService.getEid() + "is seeking to add a quiz to class " + ltiLaunchData.getCustom_canvas_course_id());
        ModelAndView mav = new ModelAndView("addQuiz");
        return mav;
    }

    /**
     * Adds all of the information and files to the server and saves it as a quiz on the database
     * @param
     * @param
     * @param classId
     * @param className
     * @param numTries
     * @param showAnswersAfterLDT
     * @param jspFile
     * @param ansFile
     * @return
     * @throws NoLtiSessionException
     * @throws RuntimeException
     */
    @RequestMapping("/uploadQuiz")
    public ModelAndView uploadQuiz(@RequestParam("classId") String classId, @RequestParam("className") String className,
                                   @RequestParam("numTries") int numTries, @RequestParam("showAnswersAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showAnswersAfterLDT,
                                   @RequestParam("jsp File") MultipartFile jspFile, @RequestParam("Answer File") MultipartFile ansFile) throws NoLtiSessionException, RuntimeException {
        LtiLaunchData ltiLaunchData = getTeacherSession();

        Date showAnswersAfter = Date.from(showAnswersAfterLDT.atZone(ZoneId.systemDefault()).toInstant());

        fileService.uploadFile(jspFile, classId, true);
        String ansFilePath = fileService.uploadFile(ansFile, classId, false).toString();
        String quizName = jspFile.getOriginalFilename().replaceFirst(".jsp","");

        LOG.info("uploading" + ansFile + "to:" + ansFilePath);
        Quiz quiz = new Quiz(quizName, ltiLaunchData.getCustom_canvas_course_id(), className, ansFilePath, numTries, showAnswersAfter);
        if (!cqs.exists(quiz.getQuizId())) {
            cqs.add(quiz);
            LOG.info(quiz.getQuizName() + "(" + quiz.getQuizId() + ") has been added to the mongo database");
        } else {
            cqs.replaceQuiz(quiz);
            LOG.info(quiz.getQuizName() + "(" + quiz.getQuizId() + ") has been replaced in the mongo database");
        }
        String success = "Your quiz " + quizName + " was added successfully";
        ModelAndView mav = new ModelAndView("teacherPage", "name", ltiLaunchData.getLis_person_name_family());
        mav.addObject("success", success);
        return mav;
    }

    /**
     * updates the grade for the user on canvas
     * @param request
     * @throws NoLtiSessionException
     * @throws IOException
     */
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

    /**
     * provides json to the jsp file so the user can get the answers for the quiz
     * @param
     * @param response
     * @throws NoLtiSessionException
     * @throws IOException
     */
    @RequestMapping(value = "/getAnswers", method = RequestMethod.GET)
    public void getAnswers( HttpServletResponse response) throws NoLtiSessionException, IOException {
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
            TreeMap<String, Question> questionsMap = quiz.getQuestionsMap();

            Iterator<Map.Entry<String, Question>> questionsItr = questionsMap.entrySet().iterator();
            int index = 0;

            while (questionsItr.hasNext() && index < questionGradesArr.length) {
                Map.Entry<String, Question> qEntry = questionsItr.next();

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

    /**
     * Provides a quiz based the long id in the url. Meant to be used for students.
     * @param request
     * @param id
     * @return
     * @throws NoLtiSessionException
     * @throws IOException
     */
    @RequestMapping(value = {"/quizResult", "/quizResult{id:[\\d]+}"})
    public ModelAndView quizResult(HttpServletRequest request, @PathVariable("id") long id) throws NoLtiSessionException{

        LtiLaunchData ltiLaunchData = getStudentSession();
        Quiz quiz = cqs.getOne(new Document("quizId", id));
        if (quiz == null)
            throw new RuntimeException("quiz does not exist");
        HttpSession session = request.getSession();
        session.setAttribute("quizId", id);
        return new ModelAndView("quizzes/" + quiz.getCourseId() + "/" + quiz.getQuizName());
    }

    /**
     * Gives a preview of a quiz to a professor
     * @param request
     * @param id
     * @return
     * @throws NoLtiSessionException
     */
    @RequestMapping(value = {"/quizPreview", "/quizPreview{id:[\\d]+}"})
    public ModelAndView quizPreview(HttpServletRequest request, @PathVariable("id") long id) throws NoLtiSessionException{
        LtiLaunchData ltiLaunchData = getTeacherSession();
        Quiz quiz = cqs.getOne(new Document("quizId", id));
        if (quiz == null)
            throw new RuntimeException("quiz does not exist");
        HttpSession session = request.getSession();
        session.setAttribute("quizId", id);
        return new ModelAndView("quizzes/" + quiz.getCourseId() + "/" + quiz.getQuizName());
    }

    /**
     * Sets up the submission to be saved, graded and added to the database. It also sets up the next view to see answers
     * and grades for the student.
     * @param request
     * @return
     * @throws NoLtiSessionException
     * @throws IOException
     */
    @RequestMapping(value = "/submitQuiz", method = RequestMethod.POST)
    public ModelAndView testQuiz(HttpServletRequest request) throws NoLtiSessionException, IOException {
        //TODO: change to student session
        LtiLaunchData ltiLaunchData = getTeacherSession();

        String userId = ltiLaunchData.getCustom_canvas_user_login_id();

        Map<String, String[]> paramsMap = request.getParameterMap();
        if(paramsMap.containsKey("pledged"))
            paramsMap.remove("pledged");
        TreeMap<String, String[]> inputsMap = new TreeMap<>(new qNameComparator());
        inputsMap.putAll(paramsMap);

        HttpSession session = request.getSession(true);
        //use real quiz id
        long quizId = (long) session.getAttribute("quizId");
        Quiz quiz = cqs.getOne(new Document("quizId", quizId));
        if (quiz == null)
            throw new RuntimeException("quiz does not exist");
        QuizSubmission quizSub = new QuizSubmission(quiz.getQuizId(), ltiLaunchData.getCustom_canvas_user_login_id(), ltiLaunchData.getLis_person_name_full(), inputsMap, quiz);
        String outcome = "your submission was added successfully";
        if (quiz.getNumTries() > cqss.getTries(new Document("quizId", quizId).append("userId", userId))) {
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
        mav.addObject("qAnswers", quiz.getAnswers());
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

    /**
     * checks that the users has an lti session and is a valid user.
     * @return
     * @throws NoLtiSessionException
     */
    private LtiLaunchData accessCheck() throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        LtiLaunchData ltiLaunchData = ltiSession.getLtiLaunchData();
        if(ltiSession.getEid()==null||ltiSession.getEid().isEmpty())
        {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        return ltiLaunchData;
    }

    /**
     * Checks that the user is a teacher and returns the ltiLaunchData to get more details
     * @return
     * @throws NoLtiSessionException
     */
    private LtiLaunchData getTeacherSession() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = accessCheck();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        if(roleList == null || !(roleList.contains(LtiLaunchData.InstitutionRole.Instructor)))
        {
            throw new AccessDeniedException("You are not an instructor and you cannot view this page. Roles: " + roleList.toString());
        }
        return ltiLaunchData;
    }

    /**
     * Checks that the user is a student and returns the ltiLaunchData to get more details
     * @return
     * @throws NoLtiSessionException
     */
    private LtiLaunchData getStudentSession() throws NoLtiSessionException {
        LtiLaunchData ltiLaunchData = accessCheck();
        List<LtiLaunchData.InstitutionRole> roleList = canvasService.getRoles();
        if(roleList == null || !(roleList.contains(LtiLaunchData.InstitutionRole.Student) || roleList.contains(LtiLaunchData.InstitutionRole.Learner)))
        {
            throw new AccessDeniedException("You are not an Student and you cannot view this page");
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
