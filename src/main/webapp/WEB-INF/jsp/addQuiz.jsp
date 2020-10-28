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
    <input class="controls" type="submit" value="Return to teacher view"/>
</form>
<form action = "uploadQuiz" method = "post" enctype = "multipart/form-data">

    <table>
        <tr>
            <th>jsp File: </th>
            <th>Answer File: </th>
            <th>Class Name: </th>
            <th>Course Id (if different from the current course): </th>
            <th>Attempts Allowed: </th>
            <th>Date to show answers after: </th>
        </tr>
        <tr>
            <td><input type = "file" id = "jspFile" name = "jsp File" required/></td>
            <td><input type = "file" id = "answerFile" name = "Answer File" required /></td>
            <td><input type = "text" id = "className" name = "className" required /></td>
            <td><input type = "text" id = "classId" name = "classId" required/></td>
            <td><input type = "number" id = "numTries" name = "numTries" required default="1"/></td>
            <td><input type ="datetime-local" id = "showAnswersAfter" name = "showAnswersAfter" required/></td>
        </tr>
    </table>
    <input type = "submit" value = "Upload Quiz" />
</form>
</body>
</html>
