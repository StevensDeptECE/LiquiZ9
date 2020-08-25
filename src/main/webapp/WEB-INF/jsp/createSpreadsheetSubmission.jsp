<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 8/12/2020
  Time: 10:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>createSpreadsheetSubmission</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Question Number</th>
        <th>Question ID</th>
        <th>User Answers</th>
        <th>Answers</th>
        <th>Grade</th>
    </tr>
    <c:forEach var="entry" items="${qInputs}" varStatus="loop">
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
            <td>${qGrades[loop.index]}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
