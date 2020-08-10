
<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 7/4/2020
  Time: 2:31 AM
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
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>view quizzes</title>
    <link rel="stylesheet" type="text/css" href="quiz.css">
</head>
<body>

<h1>Editing quizzes</h1>
<form method="get" action="QuizRequest">
    <h2>Pick which quiz you want to edit</h2>
    <div class='choice' id='choice'>
        <table class=''><tr>
            <c:if test="${not empty quizList}">
                <c:forEach items="${quizList}" var="quiz">
                    <td><input type='radio' name='quiz' value="${quiz.getQuizId()}">${quiz.getQuizName()} : Quiz Id ${quiz.getQuizId()}</td>
                </c:forEach>
            </c:if>
        </table>
        <input class='controls' type='submit' value='Submit'/>
    </div>
</form>

</body>
</html>