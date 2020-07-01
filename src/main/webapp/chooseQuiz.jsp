<%-- 
    Document   : index
    Created on : Jun 1, 2020, 1:48:22 PM
    Author     : ejone
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.File" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>index</title>
        <link rel="stylesheet" type="text/css" href="quiz.css">
    </head>
    <body>
        <form method="post" action="quizChoice">
            <h1>Pick which quiz you are taking.</h1>
            <div class='quizChoice' id='quizChoice'>
                <table class=''><tr>
              <%
                File folder = new File("../../../LiquiZ9/data/quizPages/");//");///opt/tomcat/webapps/LiquiZServer-1.0/quizPages/
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String name = file.getName().replaceFirst("[.][^.]+$", "");   
              %>
                        <td><input type='radio' name='quiz' value="<%=file.getName()%>"><%=name%></td>
              <%
                    }
                }   
              %>
                </table>    
            </div>
            <input class='controls' type='submit' value='Submit Quiz'/>
        </form>
        <form method='get' action="quizzesLoad">
            <input class='controls' type='submit' value='edit quizzes'/>
        </form>
    </body>
</html>
