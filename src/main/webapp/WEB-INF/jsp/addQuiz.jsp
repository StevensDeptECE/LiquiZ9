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
    <link rel="stylesheet" type="text/css" href='${context}/css/page.css'>
</head>
<body>
<h1>Upload a Quiz here</h1>
<form action="${context}/teacherView" method="get">
    <input class="controls" type="submit" value="Cancel"/>
</form>
<form action = "${context}/uploadQuiz" method = "post" enctype = "multipart/form-data">

    <table>
        <tr>
            <th><label for="quizName">Quiz Name: </label></th>
            <td><input type = "text" id = "quizName" name = "quiz Name" required/></td>
        </tr>
        <tr>
            <th><label  for="jspFile">jsp File:</label> </th>
            <td><input type = "file" id = "jspFile" name = "jsp File" required/></td>
        </tr>
        <tr>
            <th><label for="answerFile">Answer File:</label> </th>
            <td><input type = "file" id = "answerFile" name = "Answer File" required /></td>
        </tr>
        <tr>
            <th><label for="className">Class Name: </label></th>
            <td><input type = "text" id = "className" name = "className" required value="${courseName}" /></td>
        </tr>
        <tr>
            <th><label for="classId">Course Id (if different from the current course): </label></th>
            <td><input type = "text" id = "classId" name = "classId" required value="${courseId}"/></td>
        </tr>
        <tr>
            <th><label for="numTries"> Attempts Allowed: </label></th>
            <td><input type = "number" id = "numTries" name = "numTries" value="1"/></td>
        </tr>
        <tr>
            <th><label for="showAnswersAfter">Date to show answers after: </label></th>
            <td><input type ="datetime-local" id = "showAnswersAfter" name = "showAnswersAfter" required/></td>
        </tr>
        <tr>
        </tr>
    </table>
    <input type = "submit" value = "Upload Quiz" />
</form>
</body>
</html>
