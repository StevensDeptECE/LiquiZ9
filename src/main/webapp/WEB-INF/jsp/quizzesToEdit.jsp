<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 8/10/2020
  Time: 4:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
    Document   : viewQuizes
    Created on : Jun 24, 2020, 3:33:03 PM
    Author     : ejone
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="org.liquiz.stevens.quiz.Quiz" %>
<%@ page import="org.liquiz.stevens.quiz.Pref"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Security-Policy" content="default-src *; style-src 'self' http://* 'unsafe-inline'; script-src 'self' http://* 'unsafe-inline' 'unsafe-eval'" />
    <title>view quizzes</title>
    <link rel="stylesheet" type="text/css" href="css/page.css">
</head>
<body>

<h1>Editing quizzes</h1>
<h3>${outcome}</h3>
<form method="get" action="deleteQuizzes">
    <input class="controls" type="submit" value="Return to teacher view" formaction="teacherView"/>
    <h2>Pick which quizzes you want to delete, edit or view</h2>
    <div class='choice' id='choice'>
        <table class=''>
            <tr>
                <th>Delete</th>
                <th>Quiz</th>
                <th>Quiz Id</th>
                <th>Course ID</th>
                <th>Allowed attempts</th>
                <th>Max Grade</th>
                <th>Average Grade</th>
                <th>Answers Release Date</th>
                <th>Preview Quiz</th>
                <th>View Submissions</th>
                <th>Edit Quiz</th>
            </tr>
            <c:if test="${not empty quizList}">
            <c:forEach items="${quizList}" var="quiz" varStatus="loop">
                <tr>
                    <td> <input type='checkbox' name='quiz' value="${quiz.getQuizId()}"></td>
                    <td> ${quiz.getQuizName()}</td>
                    <td> ${quiz.getQuizId()}</td>
                    <td> ${quiz.getCourseId()}</td>
                    <td> ${quiz.getNumTries()}</td>
                    <td> ${quiz.getMaxGrade()}</td>
                    <td> ${avgGrades.get(quiz.getQuizId())}</td>
                    <td> ${Pref.getDateFormat().format(quiz.getAnswersRelease())}</td>
                    <td> <input class="controls" type="submit" name="${quiz.getQuizId()}" value="Preview Quiz" formaction="quizPreview${quiz.getQuizId()}"></td>
                    <td> <input class="controls" type="submit" name="${quiz.getQuizId()}" value="View Submissions" formaction="viewSubmissions${quiz.getQuizId()}"></td>
                    <td> <input class="controls" type="submit" name="${quiz.getQuizId()}" value="Edit Quiz" formaction="editQuiz${quiz.getQuizId()}"></td>
                </tr>

            </c:forEach>
            </c:if>
        </table>
        <input class='controls' type='submit' value='Delete Selected Quizzes'/>
    </div>
</form>

</body>
</html>