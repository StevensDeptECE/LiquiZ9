import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class TestingServlet extends HttpServlet {

 public void doGet(HttpServletRequest request,
  HttpServletResponse response)
  throws ServletException, IOException {

  PrintWriter out = response.getWriter();
  out.println("<HTML>");
  out.println("<HEAD>");
  out.println("<TITLE>Servlet Testing</TITLE>");
  out.println("</HEAD>");
  out.println("<BODY>");
  out.println("Welcome to the Servlet Testing Center");
  out.println("</BODY>");
  out.println("</HTML>");
 }
}
