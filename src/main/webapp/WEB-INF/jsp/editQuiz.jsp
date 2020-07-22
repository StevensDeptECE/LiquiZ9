%--
Document   : quizEdit
Created on : Jun 24, 2020, 4:36:14 PM
Author     : ejone
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="org.liquiz.stevens.quiz.*"%>
<%@page import="java.util.TreeMap"%>
<%@page import="org.liquiz.stevens.questions.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit quiz</title>
    <link rel="stylesheet" type="text/css" href="quiz.css">
</head>

<h1>Editing quizzes</h1>
<form method="get" action="QuizEdit">
    <h2>Edit the quiz</h2>
    <div class='quizView' id='quizView'>
        <fmt:parseNumber var="j" type="number" value="${quiz.getQuizId()}" />
        <h2><c:out value="${j}" /></h2>
        <fmt:parseDate value="${quiz.getAnswersRelease()}" var="parsedDate"  pattern="dd-MM-yyyy" />
        <h3>Answers Release: <c:out value="${parsedDate}" /></h3>
        <h3>Max Grade: ${quiz.getMaxGrade()}</h3>
        <h3>Number of Tries: ${quiz.getNumTries()}</h3>
        <h3>Answer File: ${quiz.getAnswerFile()}</h3>
        <h3>Question List: </h3>
        <%
            TreeMap<Integer, Question> questionsMap = quiz.getQuestionsMap();
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
