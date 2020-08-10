<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.File" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="media/quiz.css">
    <title>index</title>


</head>

<body>
<h1>Absolute Path is:<%= request.getServletContext().getRealPath("/") %></h1>
<h1>User.dir path is :${file}</h1>
    <h1>${success}</h1>
    <h1>Hello professor ${name}</h1>
    <p>Here is going to be the options to upload liquiz document,
    <br>view which students have taken a quiz, and the quiz question statistics.
    </p>
    <h2>options:</h2>
    <form action="showQuizzes" method="post">
        <input class='controls' type='submit' value='View Quizzes'/>
    </form>
    <form action="addQuiz" method="post">
        <input class='controls' type='submit' value='Add new quiz'/>
    </form>
</body>
</html>