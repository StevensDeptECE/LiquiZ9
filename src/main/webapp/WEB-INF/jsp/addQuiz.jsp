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
    <link rel="stylesheet" type="text/css" href='css/page.css'>
</head>
<body>
<h1>Upload a Quiz here</h1>
<form action="teacherView" method="get">
    <input class="controls" type="submit" value="Cancel"/>
</form>
<form action = "uploadQuiz" method = "post" enctype = "multipart/form-data">

    <table>
        <tr>
            <th>jsp File: </th>
            <td><input type = "file" id = "jspFile" name = "jsp File" required/></td>
        </tr>
        <tr>
            <th>Answer File: </th>
            <td><input type = "file" id = "answerFile" name = "Answer File" required /></td>
        </tr>
        <tr>
            <th>Class Name: </th>
            <td><input type = "text" id = "className" name = "className" required value="${courseName}" /></td>
        </tr>
        <tr>
            <th>Course Id (if different from the current course): </th>
            <td><input type = "text" id = "classId" name = "classId" required value="${courseId}"/></td>
        </tr>
        <tr>
            <th>Attempts Allowed: </th>
            <td><input type = "number" id = "numTries" name = "numTries" value="1"/></td>
        </tr>
        <tr>
            <th>Date to show answers after: </th>
            <td><input type ="datetime-local" id = "showAnswersAfter" name = "showAnswersAfter" required/></td>
        </tr>
        <tr>
        </tr>
    </table>
    <input type = "submit" value = "Upload Quiz" />
</form>
</body>
</html>
