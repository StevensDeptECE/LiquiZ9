
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@page import="java.util.Map"%>
<%@page import="org.liquiz.stevens.quiz.*"%>
<%@page import="java.util.TreeMap"%>
<%@page import="org.liquiz.stevens.questions.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit quiz</title>
    <link rel="stylesheet" type="text/css" href="${context}/css/page.css">
</head>

<h1>Editing quizzes</h1>
<form method="get" action="QuizEdit">
    <h2>Edit the quiz</h2>
    <div class='quizView' id='quizView'>
        <fmt:parseNumber var="j" type="number" value="${quiz.getQuizId()}" />
        <h2>Id=<c:out value="${j}" /></h2>
        <h3>Answers Release: ${quiz.getAnswersRelease()}</h3>
        <h3>Max Grade: ${quiz.getMaxGrade()}</h3>
        <h3>Number of Tries: ${quiz.getNumTries()}</h3>
        <h3>Answer File: ${quiz.getAnswerFile()}</h3>
        <h3>Question List: </h3>
        <%
            Quiz quiz = (Quiz) request.getAttribute("quiz");
            TreeMap<String, Question> questionsMap = quiz.getQuestionsMap();
            for(Map.Entry<String, Question> entry : questionsMap.entrySet()){
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
