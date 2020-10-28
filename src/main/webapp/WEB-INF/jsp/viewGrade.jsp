<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 7/17/2020
  Time: 5:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.File" %>
<%@ page import="java.util.TreeMap" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/page.css">
    <title>viewGrade</title>


</head>

<body>
<h1>${success}</h1>
<h1>Hello ${username}</h1>
<h2>Your grade is: ${grade}/${maxGrade}</h2>
<table border="1">
    <tr>
        <th>Question Number</th>
        <th>User Answer</th>
        <th>Answer</th>
        <th>Grade</th>
        <th>Max Grade</th>
    </tr>
    <c:forEach var="entry" items="${qInputs}" varStatus="loop">
    <tr>
        <td>${loop.count}</td>
        <td>

        <c:forEach var="inputsArr" items="${entry.value}">
        ${inputsArr} |
        </c:forEach>

        </td>
        <td>${qAnswers[loop.index]}</td>
        <td>${qGrades[loop.index]}</td>
        <td>${qMaxGrades[loop.index]}</td>
    </tr>
    </c:forEach>
</table>

</body>
</html>