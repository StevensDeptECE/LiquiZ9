
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <link rel="stylesheet" type="text/css" href='css/quiz.css'>
  <title>
    LiQuiz [CPE390 Floating Point]
  </title>
  <script src='js/quiz.js'></script>
</head>
<body onload='startTime(0)'>
  <form method="post" action="submitQuiz">

  <!-- Header -->
  <div id='header' class='header'>
    <img class='logo' src='media/stevenslogotrans.png'/>
    <div class='headerText'>
      <div class='quizTitle'>
        CPE390 Floating Point
      </div>

      <div class='headerDetails'>
        <div class='headerRow'>
          
        </div>
        <div class='headerRow'>
          Email    if you have any questions!
        </div>
        <div class='headerRow'>
          <input id='pledge' type='checkbox' name='pledged' value='pledged'/>
          <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>
        </div>
        <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>
        <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
      </div>
    </div>
    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>
  </div>

  <div class='section'>
    <div class='question' id='q1'>
      <div>
        1.	Exact/Inexact
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
Which of the following numbers are exactly representable in floating point?<p hidden>3</p>
1.0 <select class='dro' name='q_1_1'id='q_1_1'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>4</p>
11.5 <select class='dro' name='q_1_2'id='q_1_2'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>5</p>
17.0 <select class='dro' name='q_1_3'id='q_1_3'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>6</p>
0.1 <select class='dro' name='q_1_4'id='q_1_4'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>7</p>
0.25 <select class='dro' name='q_1_5'id='q_1_5'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>8</p>
0.125 <select class='dro' name='q_1_6'id='q_1_6'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>9</p>
0.3 <select class='dro' name='q_1_7'id='q_1_7'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>10</p>
3.75 <select class='dro' name='q_1_8'id='q_1_8'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>11</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
Which of the following numbers are exactly representable in floating point?<p hidden>3</p>
1.0 <select class='dro' name='aq_1_1'id='q_1_1'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>4</p>
11.5 <select class='dro' name='aq_1_2'id='q_1_2'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>5</p>
17.0 <select class='dro' name='aq_1_3'id='q_1_3'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>6</p>
0.1 <select class='dro' name='aq_1_4'id='q_1_4'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>7</p>
0.25 <select class='dro' name='aq_1_5'id='q_1_5'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>8</p>
0.125 <select class='dro' name='aq_1_6'id='q_1_6'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>9</p>
0.3 <select class='dro' name='aq_1_7'id='q_1_7'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>10</p>
3.75 <select class='dro' name='aq_1_8'id='q_1_8'>
            <option value=''></option>
            <option value='exact'>exact</option>
            <option value='inexact'>inexact</option>
        </select><p hidden>11</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q2'>
      <div>
        2.	Which of the following are true?
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
float x = 1.0f, y = 0.0001, z = 0.000001;<p hidden>14</p>
x + y == y + x           <select class='dro' name='q_2_1'id='q_2_1'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>15</p>
x + y + y + y + y == y + y + y + y + x   <select class='dro' name='q_2_2'id='q_2_2'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>16</p>
x + z + z  == z + z + x	     <select class='dro' name='q_2_3'id='q_2_3'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>17</p>
<p hidden>18</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
float x = 1.0f, y = 0.0001, z = 0.000001;<p hidden>14</p>
x + y == y + x           <select class='dro' id='q_2_1'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>15</p>
x + y + y + y + y == y + y + y + y + x   <select class='dro' id='q_2_2'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>16</p>
x + z + z  == z + z + x	     <select class='dro' id='q_2_3'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>17</p>
<p hidden>18</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q3'>
      <div>
        3.	Rules of Inf and NaN
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
double x = 1.0 / 0.0;  // x = <input class='' name='Q_3_1' type='text' id='Q_3_1' size='6'/><p hidden>21</p>
-1.0 / 0.0 = <input class='' name='Q_3_2' type='text' id='Q_3_2' size='6'/><p hidden>22</p>
0.0 / 0.0 = <input class='' name='Q_3_3' type='text' id='Q_3_3' size='6'/><p hidden>23</p>
50.0 / 100.0  = <input class='' name='n_3_4' type='text' id='n_3_4' size='6'/><p hidden>24</p>
sqrt(1.0 / 0.0) = <input class='' name='Q_3_5' type='text' id='Q_3_5' size='6'/><p hidden>25</p>
sqrt(-1.0) = <input class='' name='Q_3_6' type='text' id='Q_3_6' size='6'/><p hidden>26</p>
2.0 / 0. = <input class='' name='Q_3_7' type='text' id='Q_3_7' size='6'/><p hidden>27</p>
cos(1.0 / 0.0) = <input class='' name='Q_3_8' type='text' id='Q_3_8' size='6'/><p hidden>28</p>
5.0 / x  <input class='' name='n_3_9' type='text' id='n_3_9' size='6'/><p hidden>29</p>
sin(3.0 / 0.0 * 0.0) = <input class='' name='Q_3_10' type='text' id='Q_3_10' size='6'/><p hidden>30</p>
0.0 / x // <input class='' name='n_3_11' type='text' id='n_3_11' size='6'/><p hidden>31</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
double x = 1.0 / 0.0;  // x = <label class='' name='aQ_3_1' type='text' id='Q_3_1' size='6'></label><p hidden>21</p>
-1.0 / 0.0 = <label class='' name='aQ_3_2' type='text' id='Q_3_2' size='6'></label><p hidden>22</p>
0.0 / 0.0 = <label class='' name='aQ_3_3' type='text' id='Q_3_3' size='6'></label><p hidden>23</p>
50.0 / 100.0  = <label class='' name='an_3_4' type='text' id='n_3_4' size='6'></label><p hidden>24</p>
sqrt(1.0 / 0.0) = <label class='' name='aQ_3_5' type='text' id='Q_3_5' size='6'></label><p hidden>25</p>
sqrt(-1.0) = <label class='' name='aQ_3_6' type='text' id='Q_3_6' size='6'></label><p hidden>26</p>
2.0 / 0. = <label class='' name='aQ_3_7' type='text' id='Q_3_7' size='6'></label><p hidden>27</p>
cos(1.0 / 0.0) = <label class='' name='aQ_3_8' type='text' id='Q_3_8' size='6'></label><p hidden>28</p>
5.0 / x  <label class='' name='an_3_9' type='text' id='n_3_9' size='6'></label><p hidden>29</p>
sin(3.0 / 0.0 * 0.0) = <label class='' name='aQ_3_10' type='text' id='Q_3_10' size='6'></label><p hidden>30</p>
0.0 / x // <label class='' name='an_3_11' type='text' id='n_3_11' size='6'></label><p hidden>31</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q4'>
      <div>
        4.	true or false?
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
1.0 / 0.0 == -1.0 / 0.0 ? <select class='dro' name='q_4_1'id='q_4_1'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>34</p>
2.0 / 0.0 == 1.0 / 0.0 + 1 ? <select class='dro' name='q_4_2'id='q_4_2'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>35</p>
0.0 / 0.0 == sqrt(-1.0) ? <select class='dro' name='q_4_3'id='q_4_3'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>36</p>
sqrt(-1.0) != sqrt(-1.0) ? <select class='dro' name='q_4_4'id='q_4_4'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>37</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
1.0 / 0.0 == -1.0 / 0.0 ? <select class='dro' id='q_4_1'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>34</p>
2.0 / 0.0 == 1.0 / 0.0 + 1 ? <select class='dro' id='q_4_2'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>35</p>
0.0 / 0.0 == sqrt(-1.0) ? <select class='dro' id='q_4_3'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>36</p>
sqrt(-1.0) != sqrt(-1.0) ? <select class='dro' id='q_4_4'>
            <option value=''></option>
            <option value='true'>true</option>
            <option value='false'>false</option>
        </select><p hidden>37</p></pre>
    </div>
  </div>
  
  
    <div class='footer'>
      <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>
      <input class='controls' type='submit' value='Submit Quiz' onClick='showResult()'/>
    </div>
    </form>
</body>
</html>
      