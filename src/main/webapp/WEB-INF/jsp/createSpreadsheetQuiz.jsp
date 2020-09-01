<%@ page import="org.liquiz.stevens.quiz.Quiz" %><%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 8/10/2020
  Time: 5:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table border="1" cellpadding="3" cellspacing="3">
        <tr>
            <th>Full Name</th>
            <th>Student Id</th>
            <th>Grade</th>
            <c:forEach begin="0" end="${quiz.getNumQuestions()-1}" varStatus="loop">
                <th>Q${loop.count}</th>
            </c:forEach>
        </tr>
        <%
            Quiz quiz = (Quiz) request.getAttribute("quiz");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "inline; filename=" + quiz.getQuizName().replaceAll(" ","") + ".xls");
        %>
        <c:forEach var="quizSub" items="${quizSubList}">
            <tr>
            <td>${quizSub.getFullName()}</td>
            <td>${quizSub.getUserId()}</td>
            <td>${quizSub.getGrade()}</td>
            <c:forEach var="grade" items="${quizSub.getQuestionGrades()}">
                <td>${grade}</td>
            </c:forEach>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
