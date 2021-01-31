<%--@elvariable id="outcome" type="java.lang.String"--%>
<%--@elvariable id="context" type="java.lang.String"--%>
<jsp:useBean id="quizList" scope="request" type="java.util.List<org.liquiz.stevens.quiz.Quiz >"/>
<jsp:useBean id="avgGrades" scope="request" type="java.util.Map<java.lang.Long, java.lang.Long>"/>
<jsp:useBean id="submissionCounts" scope="request" type="java.util.Map<java.lang.Long, java.lang.Long>"/>
<%--
  Created by IntelliJ IDEA.
  User: ejone
  Date: 8/10/2020
  Time: 4:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--
    Document   : viewQuizes
    Created on : Jun 24, 2020, 3:33:03 PM
    Author     : ejone
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Security-Policy"
          content="default-src *; style-src 'self' http://* 'unsafe-inline'; script-src 'self' http://* 'unsafe-inline' 'unsafe-eval'"/>
    <title>view quizzes</title>
    <link rel="stylesheet" type="text/css" href="${context}/css/page.css">
</head>
<body>

<h1>Editing quizzes</h1>
<h3>${outcome}</h3>
<form action="${context}/addQuiz" method="post">
    <input class='controls' type='submit' value='Add new quiz'/>
</form>
<form method="get" action="deleteQuizzes">
    <h2>Pick which quizzes you want to delete, edit or view</h2>
    <div class='choice' id='choice'>
        <table class=''>
            <tr>
                <th>Delete</th>
                <th>Quiz</th>
                <th>Quiz Id</th>
                <th>Course ID</th>
                <th>Canvas Assignment ID</th>
                <th>Allowed attempts</th>
                <th>Max Grade</th>
                <th>Average Grade</th>
                <th>Answers Release Date</th>
                <th>Preview Quiz</th>
                <th>View Submissions</th>
                <th>Submissions</th>
                <th>Edit Quiz</th>
            </tr>
            <c:if test="${not empty quizList}">
            <c:forEach items="${quizList}" var="quiz" varStatus="loop">


                <tr>
                    <td> <input type='checkbox' id='quiz-${quiz.quizId}' name='quiz-${quiz.quizId}' value="${quiz.quizId}"></td>
                    <td> <label for="quiz-${quiz.quizId}">${quiz.quizName}</label></td>
                    <td> ${quiz.quizId}</td>
                    <td> ${quiz.courseId}</td>
                    <td> ${quiz.assignmentId}</td>
                    <td> ${quiz.numTries}</td>
                    <td> ${quiz.maxGrade}</td>
                    <td> ${avgGrades.get(quiz.quizId)}</td>
                    <td> <fmt:formatDate pattern="yyyy.MM.dd" value="${quiz.answersRelease}" /></td>
                    <td> <input class="controls" type="submit" name="${quiz.quizId}" value="Preview Quiz" formaction="quizPreview${quiz.quizId}"></td>
                    <td> <input class="controls" type="submit" name="${quiz.quizId}" value="View Submissions" formaction="viewSubmissions${quiz.quizId}"></td>
                    <td> ${submissionCounts.get(quiz.quizId)}</td>
                    <td> <input class="controls" type="submit" name="${quiz.quizId}" value="Edit Quiz" formaction="editQuiz${quiz.quizId}"></td>
                </tr>

            </c:forEach>
            </c:if>
        </table>
        <input class='controls' type='submit' value='Delete Selected Quizzes'/>
    </div>
</form>

</body>
</html>