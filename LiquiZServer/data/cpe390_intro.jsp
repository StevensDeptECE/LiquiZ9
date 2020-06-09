<%-- 
    Document   : cpe390_intro
    Created on : Jun 3, 2020, 11:45:56 AM
    Author     : ejone
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>
    "Intro"
  </title>
  <link rel="stylesheet" type="text/css" href="quiz.css">
</head>
<body onload='startTime()'>
  <form method="post" action="loginTest">
    <div id='header' class='header'>
      <div style='margin-left: 250px'>
        <table>
          <tr><td class='headtext'>Userid</td><td><input class='ctrl' id='q1_1' type='text' name='q1_1'/></td><td></td></tr>
          <tr><td class='headtext'>Passwd</td><td><input class='ctrl' id='q1_2' type='password' name='q1_2'/></td></tr>
          <tr><td class='headtext'>Name</td><td><input class='ctrl' id='Q1_3' type='text' name='Q1_3'/></td></tr>
          <tr><td><input class='ctrl' id='q1_4' type='checkbox' name='q1_4'/></td>
            <td class='headtext' colspan='2'>I pledge my honor that I have abided by the Stevens Honor System</td></tr>
            <tr><td class='headtext'>Time Remaining</td><td id='topTime' class='time'></td><td><input id='audioControl' class='controls' type='button' value='turn audio ON' onClick='scheduleAudio()'/>
            </td></tr>
          </table>
      </div>
    </div>
    <div class='q' id='q2'>2. Watch and React<span class='pts'> (10 points)</span>
      <pre>Watch the movie on the Tacoma Narrows Bridge.
          Should engineers have known the bridge would collapse?
          What caused the collapse?
        <video controls width='320' height='240'><source src='TacomaNarrowsBridge.mp4' type='video/mp4'></video>
        <textarea rows='20' cols='80' id='m2_1' name='m2_1'> Type your essay here </textarea>
      </pre>
    </div>
    <div class='q' id='q3'>3. Simple Math<span class='pts'> (10 points)</span><pre>What is 2+2?</pre>
      <table class='mch'><tr>
        <td><input class='mc' type='radio' name='q3_1' value="2">2</td>
        <td><input class='mc' type='radio' name='q3_1' value="3">3</td>
        <td><input class='mc' type='radio' name='q3_1' value="4">*4</td>
        <td><input class='mc' type='radio' name='q3_1' value="5">5</td>
        <td><input class='mc' type='radio' name='q3_1' value="1">1</td></tr>
      </table>    
    </div>
    <div class="" id="q4">4. Simple Math what is 2.1+2.1
        <div style="color: #FF0000;">${errorMessage}</div>
        <textarea rows='1' cols='3' id='n4_1' name='n4_1'></textarea>
        <%
            try {
                double amount = Double.parseDouble(request.getParameter("n4_1"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "not a valid number");
            }
        %>
    </div>
      <div class='controls'>
        <div style='position: flow'>Time Remaining</div>
        <div id='bottomTime' class='time'></div>
        <input class='controls' type='submit' value='Submit Quiz'/>
      </div>
  </form>
</body>
</html>
