<%-- 
    Document   : quizEdit
    Created on : Jun 24, 2020, 4:36:14 PM
    Author     : ejone
--%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="questions.Question"%>
<%@page import="java.util.TreeMap"%>
<%@page import="quiz.Quiz"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit quiz</title>
        <link rel="stylesheet" type="text/css" href="WEB-INF/jsp/quiz.css">
    </head>
    <%
        Quiz quiz = (Quiz) session.getAttribute("quiz");
        Date date = quiz.getAnswersRelease();
        String release = new Date().toString();//date.toString();
    %>
        
        <h1>Editing quizzes</h1>
        <form method="get" action="QuizEdit">
            <h2>Edit the quiz</h2>
            <div class='quizView' id='quizView'>
                <h2>${quiz.getQuizId()}</h2>
                <h3>Answers Release: ${release}</h3>
                <h3>Max Grade: ${quiz.getMaxGrade()}</h3>
                <h3>Number of Tries: ${quiz.getNumTries()}</h3>
                <h3>Answer File: ${quiz.getAnswerFile()}</h3>
                <h3>Question List: </h3>
                <%
                    TreeMap<Integer,Question> questionsMap = quiz.getQuestionsMap();
                    for(Map.Entry<Integer, Question> entry : questionsMap.entrySet()){
                        Question q = entry.getValue(); 
                        String name = q.getName();
                        String type = q.type();
                        String gVal = Double.toString(q.getGradeValue());
                        String answer = q.getAnswer();
                %>
                    <h4>Id: <%=name%></h4>
                    <h4>Type: <%=type%></h4>
                    <h4>Grade Value: <%=gVal%></h4>
                    <h4>Answer(s): <%=answer%></h4>
                <%
                    }
                %> 
                <input class='controls' type='submit' value='Submit'/>
            </div>
        </form>
        
    </body>
</html>
