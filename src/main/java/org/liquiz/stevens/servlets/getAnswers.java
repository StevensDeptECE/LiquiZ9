/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.servlets;

import com.mongodb.MongoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.liquiz.stevens.mongodb.CodecQuizService;
import org.liquiz.stevens.mongodb.CodecQuizSubmissionService;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.liquiz.stevens.questions.Question;
import org.liquiz.stevens.quiz.Quiz;
import org.liquiz.stevens.quiz.QuizSubmission;

/**
 *
 * @author ejone
 */
@WebServlet(name = "getAnswers", urlPatterns = {"/getAnswers"})
public class getAnswers extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          PrintWriter out = response.getWriter();
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          response.addHeader("Access-Control-Allow-Origin", "*");
          //TODO: check if valid request
          MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
          CodecQuizService cqs = new CodecQuizService(mongo);
          CodecQuizSubmissionService cqss = new CodecQuizSubmissionService(mongo);
          
          String userId = "ejones";
          String quizId = "demo";
          
          Quiz quiz = cqs.getOne(new Document("quizId", quizId));
          QuizSubmission quizSubmission= cqss.getOne(new Document("quizId", quizId).append("userId", userId));
          
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
          /*
          JSONArray jsonArr = new JSONArray();
          JSONObject obj = new JSONObject();
          double[] darr = {10, 15, 45,50, 12.5, 12.5, 12.5, 12.5, 2.5,2.5,2.5,2.5,10.0,3.333,3.333,3.333,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,1.25,5.0,10.0,10.0,10.0,10.0,10.0,10.0};
          String[] arrId = {"q_1_1","q_2_2", "m_3_3", "m_4_4", "q_5_5","q_5_6","q_5_7","q_5_8","Q_6_9","Q_6_10","Q_6_11","q_6_12","q_7_13","q_8_14","q_8_15","q_8_16","Q_9_17","Q_9_18","Q_9_19","Q_9_20",
              "Q_9_21","q_9_22","q_9_23","q_9_24","Q_9_25","q_9_26","q_9_27","q_9_28","Q_9_29","q_9_30","q_9_31","q_9_32","q_10_33","q_11_34","q_12_35","q_12_36","q_12_37","q_12_38","q_12_39"};
          String[] arr = {"cat","Kruger","Kruger,Favardin,Song,Lu","Hoboken,United States,New Jersey","example","example","example","example","pc","pc","lr","share the same bits","N/A","O(log(n))","b","gcd","00000000","000102b8","ffffffff","000102bc","00000000","1","1","0","000000000","0","0","0","ffffffff","1","0","1","AND","N/A","kind of","instance of","instantiate","instantiation","encapsulation"};
          for(int i = 0; i < 39; i++){
              obj.put("id", arrId[i]);
              obj.put("pointsT", darr[i]);
              obj.put("pointsE", darr[i]);
              obj.put("answers", arr[i]);
              jsonArr.add(obj);
              obj = new JSONObject();
          }
          */
          
        out.print(jArr);
        out.flush();
          
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
