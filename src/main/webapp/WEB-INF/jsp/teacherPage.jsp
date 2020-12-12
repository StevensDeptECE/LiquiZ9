<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.File" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="context" value="${pageContext.request.contextPath}" />
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Security-Policy" content="style-src 'self' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com;">
    <link rel="stylesheet" type="text/css" href="${context}/css/page.css">
    <title>index</title>


</head>

<body>
    <h1>${success}</h1>
    <h1>Hello professor ${name}</h1>
    <p>Here is going to be the options to upload liquiz document,
    <br>view which students have taken a quiz, and the quiz question statistics.
    </p>
    <h2>options:</h2>
    <form action="${context}/addQuiz" method="post">
        <input class='controls' type='submit' value='Add new quiz'/>
    </form>
    <form action="${context}/quizzesToEdit" method="post">
        <input class="controls" type="submit" value="Edit Quizzes"/>
    </form>
</body>
</html>