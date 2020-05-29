/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quiz;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;

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
      //set response to html
      response.setContentType("text/html");

      // formatting html file and adding your answers
      try(PrintWriter out = response.getWriter()) {
          // formatting html file and adding your answers
          out.println("<!DOCTYPE html>");
          out.println("<html>");
          out.println("<head><title></title></head>");
          out.println("<body>");
          out.println("<h1>Your answers</h1>");
          //creates an enumeration of every parameter name that is an input field for the form.
          Enumeration forms = request.getParameterNames();
          Map<String, String[]> paramsMap = request.getParameterMap();
          HashMap<String,String[]> inputsMap = new HashMap<>();
          inputsMap.putAll(paramsMap);
          String userID = paramsMap.get("q1_1")[0];
          out.println("<h2>userid: " + userID + "</h2>");
          QuizToGrade quiz = new QuizToGrade("../../../Liquiz9/LiquiZServer/data/answerFiles/cpe390_intro.ans", inputsMap);
          Double grade = quiz.getGrade();
          Boolean graded = quiz.isGraded();
          String quizName = "quiz1";
          if(graded)
              out.println("<h2>Your grade is " + grade + "</h2>");
          try{
              File quizFolder = new File("../../../Liquiz9/LiquiZServer/data/studentAnswers/" + quizName);
              if(!quizFolder.exists()){
                  quizFolder.mkdir();
              }
              try (FileWriter myWriter = new FileWriter("../../../LiquiZServer/data/studentAnswers/" + quizName + "/" + userID + ".txt")) {
                  while(forms.hasMoreElements()){
                      //creates an object from the element in forms and sets it to a string obj
                      Object objOri = forms.nextElement();
                      String paramName = (String)objOri;
                      //sets the string param to the value for a given input field defined by the name
                      String param = request.getParameterValues(paramName)[0];
                      
                      String htmlParam = "<h2>" + paramName + ": " + param + "</h2>";
                      out.println(htmlParam);
                      
                      myWriter.write(paramName + ": " + param + "\n");
                  }
                  myWriter.write("grade: " + grade);
              }
          }
          catch(IOException e){
              System.out.println("An error occurred.");
          }
          String currentDirectory = System.getProperty("user.dir");
          out.println("<h2>The current working directory is " + currentDirectory + "</h2>");
          //D:\Research\Tomcat\Tomcat\bin
          out.println("</body>");
      }  }

}