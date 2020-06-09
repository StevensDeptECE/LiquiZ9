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
        String filename = request.getParameter("quiz");
        String name = filename.replaceFirst("[.][^.]+$", "");
        HttpSession session=request.getSession();  
        session.setAttribute("quizName", name); 
        response.sendRedirect(filename);
    }

}
