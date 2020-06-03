/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
/**
 *
 * @author ejone
 */
@WebServlet(name = "quizChoice", urlPatterns = {"/quizChoice"})
public class quizChoice extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /*response.setContentType("text/html;charset=UTF-8");
        
        try(PrintWriter out = response.getWriter()) {
          //TODO: incorporate jsp file
          out.println("<!DOCTYPE html>");
          out.println("<html>");
          out.println("<head><title></title></head>");
          out.println("<body>");
        
          String filename = request.getParameter("quiz");
          
          out.println("<h2>Your grade is " + filename + "</h2>");
          String currentDir = System.getProperty("user.dir");
          out.println("<h2>Current dir using System:" +currentDir + "</h2>");
          out.println("</body>");
        } catch(IOException e) {
              System.out.println("An error occurred.");
        }
        
        String filename = request.getParameter("quiz");
        RequestDispatcher view = request.getRequestDispatcher("../../../LiquiZServer/data/quizPages/" + filename);
        view.forward(request, response);
        */
        String filename = request.getParameter("quiz");
        response.sendRedirect(filename);//"../data/quizPages/cpe390_intro.html");"/WEB-INF/pages/" +
    }

}
