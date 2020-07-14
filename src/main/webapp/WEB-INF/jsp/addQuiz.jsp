<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 7/4/2020
  Time: 2:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addQuiz</title>
    <link rel="stylesheet" type="text/css" href='quiz.css'>
</head>
<body>
<h1>Upload a Quiz here</h1>
<form action = "uploadQuiz" method = "post" enctype = "multipart/form-data">

    <label for="jsp File">jsp File:</label>
    <input type = "file" id = "jsp File" name = "jsp File"/>
    <br />
    <label for="Answer File">Answer File:</label>
    <input type = "file" id = "Answer File" name = "Answer File" />
    <br />
    <label for="Quiz Name">Quiz Name:</label>
    <input type = "text" id = "Quiz Name" name = "quizName"/>
    <br />
    <label for="Class Name">Class Name:</label>
    <input type = "text" id = "Class Name" name = "className"/>
    <br />
    <label for="ClassId">Class Id:</label>
    <input type = "text" id = "ClassId" name = "classId"/>
    <br />
    <label for="NumTries">Number of attempts allowed:</label>
    <input type = "number" id = "NumTries" name = "numTries"/>
    <br />
    <label for="ShowAnswersAfter">Date to show answers after:</label>
    <input type ="datetime-local" id = "showAnswersAfter" name = "showAnswersAfter"/>
    <br />
    <input type = "submit" value = "Upload Quiz" />
</form>
</body>
</html>
