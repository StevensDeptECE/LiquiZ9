import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.ArrayList;
import java.util.Enumeration;

import GradeQuiz.*;

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
        int num = 1;
        int loop = 1;

        student temp = new student();
        Enumeration forms = request.getParameterNames();

        while(forms.hasMoreElements()) {
            String paramName = (String)forms.nextElement();
            String[] param = request.getParameterValues(paramName);
            if(param.length == 1 && param[0].equals("")){
                param[0] = "*blank*";
            }
            switch(loop){
                case 1://input userid
                    temp.setId(param[0]);
                    ++loop;
                    break;
                case 2://input password
                    temp.setPasswd(param[0]);
                    ++loop;
                    break;
                case 3://input name
                    temp.setName(param[0]);
                    ++loop;
                    break;
                case 4://input the status if the student check the box
                    temp.setCheckBox(param[0]);
                    ++loop;
                    break;
            }
            if(loop > 4 && param.length == 1){//single choice and fill in blank
                temp.setStuAnswer(param[0]);
                ++num;
            }else if(loop > 4 && param.length > 1){// mutiple choice
                String result = "";
                for(int i = 0; i < param.length; ++i){
                    result += param[i];
                }
                temp.setStuAnswer(result);
                ++num;
            }
            if(loop > 4 && num > 1){
                String htmlParam = "<h2>" + "Question " + num + " : " + temp.getStuAnswer().get(num-2) + "</h2>";
                out.println(htmlParam);
            }else{
                String htmlParam = "<h2>" +paramName + ": " + param[0] + "</h2>";
                out.println(htmlParam);
            }
        }
        GradeQuiz quiz = new GradeQuiz(temp);
        temp.print();
        out.println("<h2>Your grade is " +temp.getGrades()+ "</h2>");
        out.println("</body>");
        out.close();
    }

}
