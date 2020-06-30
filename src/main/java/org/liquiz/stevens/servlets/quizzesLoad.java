/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.mongodb.MongoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mongodb.CodecQuizService;
import quiz.Quiz;

/**
 *
 * @author ejone
 */
@WebServlet(name = "quizzesLoad", urlPatterns = {"/quizzesLoad"})
public class quizzesLoad extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession(false);  
        MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
        CodecQuizService cqs = new CodecQuizService(mongo);
        List<Quiz> quizList = cqs.list();
          for(Quiz q : quizList){
              System.out.println(q.getQuizId());
        }
        
        ArrayList<Quiz> quizArr = (ArrayList<Quiz>) cqs.list();
        for(Quiz q : quizArr){
              System.out.println(q.getQuizId());
        }
        session.setAttribute("quizList", quizArr); 
        response.sendRedirect("viewQuizzes.jsp");
        }
    }
