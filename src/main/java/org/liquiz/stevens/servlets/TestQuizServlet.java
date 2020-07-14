/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.Date;
import java.util.TreeMap;
import org.liquiz.stevens.mongodb.CodecQuizService;
import org.liquiz.stevens.mongodb.CodecQuizSubmissionService;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.liquiz.stevens.quiz.Quiz;
import org.liquiz.stevens.quiz.QuizSubmission;

public class TestQuizServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    // do post when get is called
    doPost(request, response);
  }

    /**
     *
     * @param request   a html form that has multiple inputs for graded quiz
     * @param response  returns an html page to the user that displays their grade
     * @throws IOException  cannot find the answer file 
     * @throws ServletException
     */
    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

      response.setContentType("text/html");
          response.setCharacterEncoding("UTF-8");
          Map<String, String[]> paramsMap = request.getParameterMap();
          TreeMap<String, String[]> inputsMap = new TreeMap<>();
          inputsMap.putAll(paramsMap);
         
          HttpSession session=request.getSession(false);  
          String quizName=(String)session.getAttribute("quizName");
          String userId = "ejones";//(String) session.getAttribute("userId");
          String classId = "c++";//String session.getAttribute("classId");
          //TODO: get userID from lti session
          MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
          CodecQuizService cqs = new CodecQuizService();
          CodecQuizSubmissionService cqss = new CodecQuizSubmissionService();
          Quiz quiz = cqs.getOne(new Document("quizId", quizName));
          if(quiz==null) {
            quiz = new Quiz(quizName, "c++","wow",  "../../../LiquiZ9/LiquiZServer/data/answerFiles/" + quizName + ".ans", 1, new Date());////"opt/tomcat/webapps/LiquiZServer-1.0/answerFiles/"
            cqs.add(quiz);
          }
          //QuizSubmission quizSub = cqss.get
          
          if(quiz.getNumTries() > cqss.getTries(new Document("quizId", quizName).append("userId", userId))){
            QuizSubmission quizSub = cqss.getOne(new Document("quizId", quizName).append("userId", userId));
            if(quizSub==null) {
              quizSub = new QuizSubmission(quizName, "ejones", inputsMap, quiz);
              cqss.add(quizSub);
            }
          }
          else
             System.out.println("no tries left");
          
          
          RequestDispatcher rd = request.getRequestDispatcher("index.jsp"); 
          
          rd.forward(request, response); 
      }  
  
  
      protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          doPost(request, response);
          
        }

}