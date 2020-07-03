<%-- 
    Document   : viewQuizes
    Created on : Jun 24, 2020, 3:33:03 PM
    Author     : ejone
--%>

<%@page import="org.liquiz.stevens.quiz.Quiz"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>view quizzes</title>
        <link rel="stylesheet" type="text/css" href="WEB-INF/jsp/quiz.css">
    </head>
    <body>
        
        <h1>Editing quizzes</h1>
        <form method="get" action="QuizRequest">
            <h2>Pick which quiz you want to edit</h2>
            <div class='choice' id='choice'>
                <table class=''><tr>
                        <%
                            ArrayList<Quiz> quizList = (ArrayList<Quiz>) session.getAttribute("quizList");
                            for(Quiz quiz : quizList){
                                String name = quiz.getQuizId();
                        %>
                        <td><input type='radio' name='quiz' value="<%=name%>"><%=name%></td>
                        <%
                            }
                        %>
                </table>        
                <input class='controls' type='submit' value='Submit'/>
            </div>
        </form>
        
    </body>
</html>
