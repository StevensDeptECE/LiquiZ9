<%@ page import="java.util.TreeMap"%>
<%@ page import="org.liquiz.stevens.quiz.QuizSubmission" %><%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 8/12/2020
  Time: 10:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>createSpreadsheetSubmission</title>
</head>
<body>
<table cellpadding="3" cellspacing="3" border="1">
    <tr>
        <th>Question Number</th>
        <th>Question ID</th>
        <th>User Answers</th>
        <th>Answers</th>
        <th>Grade</th>
    </tr>
    <%
        QuizSubmission quizSub = (QuizSubmission) request.getAttribute("quizSub");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "inline; filename=" + quizSub.getFullName().replaceAll(" ","") + ".xls");
    %>
    <c:forEach var="entry" items="${quizSub.getInputs()}" varStatus="loop">
        <tr>
            <td>${loop.count}</td>
            <td>${entry.key}</td>
            <td>
                {
                <c:forEach var="inputsArr" items="${entry.value}">
                    ${inputsArr},
                </c:forEach>
                }
            </td>
            <td>${quizAnswers[loop.index]}</td>
            <td>${quizSub.getQuestionGrade(loop.index)}</td>
        </tr>

    </c:forEach>
    <tr>
        <th>Total</th>
        <td/>
        <td/>
        <td/>
        <td>${quizSub.getGrade()}</td>
    </tr>
</table>

</body>
</html>
