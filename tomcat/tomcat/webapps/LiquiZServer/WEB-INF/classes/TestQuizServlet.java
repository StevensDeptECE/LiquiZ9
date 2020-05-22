import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Enumeration;
import java.util.ArrayList;
//import classes.quiztograde.*;

@WebServlet("/loginTest")
public class TestQuizServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    // do post when get is called
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      //set response to html
      response.setContentType("text/html");

      PrintWriter out = response.getWriter();

      // formatting html file and adding your answers
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title></title></head>");
      out.println("<body>");
      out.println("<h1>Your answers</h1>");

      //creates an enumeration of every parameter name that is an input field for the form.
      Enumeration forms = request.getParameterNames();

      QuizToGrade quiz = new QuizToGrade("cpe390_introans");

      while(forms.hasMoreElements()){
        //creates an object from the element in forms and sets it to a string obj
        Object objOri = forms.nextElement();
        String paramName = (String)objOri;
        //sets the string param to the value for a given input field defined by the name
        String param = request.getParameterValues(paramName)[0];

        quiz.addInputs(param);

        String htmlParam = "<h2>" +paramName + ": " + param + "</h2>";
        out.println(htmlParam);
      }

      quiz.updateGrade();
      Double grade = quiz.getGrade();
      out.println("<h2>Your grade is" + grade + "</h2>");

      out.println("</body>");
      out.close();
  }

}
