<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${context}/css/quiz.css"/>
</head>
<body>
<template id="quiz-content">
    ${quiz.content}
</template>
<form method="get" action="/submitAnswer" data-quizId="${quiz.quizId}">
<div id='header' class='header'>
    <img class='logo' src='${context}/media/stevenslogotrans.png'/>
    <div class='headerText'>
        <div class='quizTitle'>
            ${quiz.quizName}
        </div>

        <div class='headerDetails'>
            <div class='headerRow'>

            </div>
            <div class='headerRow'>
                Email if you have any questions!
            </div>
            <div class='headerRow'>
                <input id='pledge' type='checkbox' name='pledged' value='pledged'/>
                <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>
            </div>
            <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>
            <input class='controls' type='button' value='Submit Quiz' onClick='checkAndSubmit()'/>
        </div>
    </div>
    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>
</div>
<div id="root"></div>
</form>
<div class='footer'>
    <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>
    <input class='controls' type='button' value='Submit Quiz' onClick='checkAndSubmit()'/>
</div>

<script>
    (function () {
        let template = document.getElementById("quiz-content");
        let root = document.getElementById("root");
        let sections = template.content.querySelectorAll("div.section");
        for (let i = 0; i < sections.length; i++) {
            root.appendChild(sections[i].cloneNode(true));
        }
    })();
</script>
<script src="${context}/js/quiz.js"></script>
</body>
</html>
