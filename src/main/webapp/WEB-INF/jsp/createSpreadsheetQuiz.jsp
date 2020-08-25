<%--
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
    <table>
        <tr>
            <th>Full Name</th>
            <th>Student Id</th>
            <th>Grade</th>
            <c:forEach begin="0" end="${quiz.getNumQuestions()}" varStatus="loop">
                <th>Q${loop.count}</th>
            </c:forEach>
        </tr>
        <c:forEach var="quizSub" items="${quizSubList}">
            <td>${quizSub.getFullName()}</td>
            <td>${quizSub.getUserId()}</td>
            <td>${quizSub.getGrade()}</td>
            <c:forEach var="grade" items="${quizSub.getQuestionGrades()}">
                <td>${grade}</td>
            </c:forEach>
        </c:forEach>
    </table>
</body>
</html>
