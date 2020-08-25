
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <link rel="stylesheet" type="text/css" href='media/quiz.css'>
  <title>
    LiQuiz [2020B Midterm Practice]
  </title>
  <script src='js/quiz.js'></script>
</head>
<body onload='startTime(0)'>
  <form method="get" action="submitQuiz"></form>

  <!-- Header -->
  <div id='header' class='header'>
    <img class='logo' src='media/stevenslogotrans.png'/>
    <div class='headerText'>
      <div class='quizTitle'>
        2020B Midterm Practice
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
        1.	Counting clock Cycles
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
For a 1Ghz ARM processor, assuming each instruction takes 1 clock cycle, how long will the following code take (in nanoseconds)<p hidden>3</p>
mov r0, #4<p hidden>4</p>
mov r1, #6<p hidden>5</p>
and r2, r1, r0<p hidden>6</p>
add r3, r1, r1<p hidden>7</p>
<p hidden>8</p>
<input class='' name='Q_1_1' type='text' id='Q_1_1' size='6'/><p hidden>9</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
For a 1Ghz ARM processor, assuming each instruction takes 1 clock cycle, how long will the following code take (in nanoseconds)<p hidden>3</p>
mov r0, #4<p hidden>4</p>
mov r1, #6<p hidden>5</p>
and r2, r1, r0<p hidden>6</p>
add r3, r1, r1<p hidden>7</p>
<p hidden>8</p>
<input class='' name='aQ_1_1' type='text' id='Q_1_1' size='6'/><p hidden>9</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q2'>
      <div>
        2.	Counting clock Cycles2
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
For a 1Ghz ARM processor, assuming each instruction takes 1 clock cycle, how long will the following code take (in seconds)<p hidden>12</p>
       mov  r0, #1500000000 @ 1.5 billion<p hidden>13</p>
1:     subs r0, #1<p hidden>14</p>
       bne  1b<p hidden>15</p>
<p hidden>16</p>
<input class='' name='n_2_1' type='text' id='n_2_1' size='6'/><p hidden>17</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
For a 1Ghz ARM processor, assuming each instruction takes 1 clock cycle, how long will the following code take (in seconds)<p hidden>12</p>
       mov  r0, #1500000000 @ 1.5 billion<p hidden>13</p>
1:     subs r0, #1<p hidden>14</p>
       bne  1b<p hidden>15</p>
<p hidden>16</p>
<input class='' name='an_2_1' type='text' id='n_2_1' size='6'/><p hidden>17</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q3'>
      <div>
        3.	Explain
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
The following code will take significantly longer than 1 clock cycle per instruction. Explain why.<p hidden>20</p>
      mov  r1, #1000000<p hidden>21</p>
1:<p hidden>22</p>
      ldr  r2, [r0]<p hidden>23</p>
      add  r0, #4<p hidden>24</p>
      subs r1, #1<p hidden>25</p>
      bne  1b<p hidden>26</p>
<textarea rows='10' cols='60' name='T_3_1' id='T_3_1'>-explain here-</textarea><p hidden>27</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
The following code will take significantly longer than 1 clock cycle per instruction. Explain why.<p hidden>20</p>
      mov  r1, #1000000<p hidden>21</p>
1:<p hidden>22</p>
      ldr  r2, [r0]<p hidden>23</p>
      add  r0, #4<p hidden>24</p>
      subs r1, #1<p hidden>25</p>
      bne  1b<p hidden>26</p>
<textarea rows='10' cols='60' name='aT_3_1' id='T_3_1'>-explain here-</textarea><p hidden>27</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q4'>
      <div>
        4.	Write c++ code
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
Write a c++ function that accepts two double precision arrays and a length, multiples each corresponding element of the arrays and adds up the products. For example:<p hidden>30</p>
double a[] = {2.0, 3.0, 4.0};<p hidden>31</p>
double b[] = {3.0, 2.0, 1.5};<p hidden>32</p>
cout << sumprod(a, b, 3) << '\n';<p hidden>33</p>
<p hidden>34</p>
should print 2*3 + 3*2 + 4*1.5 = 18.0<p hidden>35</p>
<textarea rows='10' cols='60' name='T_4_1' id='T_4_1'>-type your code here-</textarea><p hidden>36</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
Write a c++ function that accepts two double precision arrays and a length, multiples each corresponding element of the arrays and adds up the products. For example:<p hidden>30</p>
double a[] = {2.0, 3.0, 4.0};<p hidden>31</p>
double b[] = {3.0, 2.0, 1.5};<p hidden>32</p>
cout << sumprod(a, b, 3) << '\n';<p hidden>33</p>
<p hidden>34</p>
should print 2*3 + 3*2 + 4*1.5 = 18.0<p hidden>35</p>
<textarea rows='10' cols='60' name='aT_4_1' id='T_4_1'>-type your code here-</textarea><p hidden>36</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q5'>
      <div>
        5.	Complete the ARM assembler program
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
This subroutine should read through an array of integers, loading<p hidden>39</p>
every element from memory into a register<p hidden>40</p>
      @ r0 points to an array of integers<p hidden>41</p>
      @ r1 = the length of the array<p hidden>42</p>
_Z1fPKii:<p hidden>43</p>
1:<p hidden>44</p>
      <input class='' name='Q_5_1' type='text' id='Q_5_1' size='3'/>  r2, [<input class='' name='Q_5_2' type='text' id='Q_5_2' size='6'/>]<p hidden>45</p>
      add  r0, #<input class='' name='Q_5_3' type='text' id='Q_5_3' size='6'/><p hidden>46</p>
      <input class='' name='Q_5_4' type='text' id='Q_5_4' size='4'/> r1, #1<p hidden>47</p>
      bne  <input class='' name='Q_5_5' type='text' id='Q_5_5' size='6'/><p hidden>48</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
This subroutine should read through an array of integers, loading<p hidden>39</p>
every element from memory into a register<p hidden>40</p>
      @ r0 points to an array of integers<p hidden>41</p>
      @ r1 = the length of the array<p hidden>42</p>
_Z1fPKii:<p hidden>43</p>
1:<p hidden>44</p>
      <input class='' name='aQ_5_1' type='text' id='Q_5_1' size='3'/>  r2, [<input class='' name='aQ_5_2' type='text' id='Q_5_2' size='6'/>]<p hidden>45</p>
      add  r0, #<input class='' name='aQ_5_3' type='text' id='Q_5_3' size='6'/><p hidden>46</p>
      <input class='' name='aQ_5_4' type='text' id='Q_5_4' size='4'/> r1, #1<p hidden>47</p>
      bne  <input class='' name='aQ_5_5' type='text' id='Q_5_5' size='6'/><p hidden>48</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q6'>
      <div>
        6.	Complete the ARM assembler program
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
Complete the following code so that it sums the array of integers at r0<p hidden>51</p>
      @ r0 points to an array of integers<p hidden>52</p>
      @ r1 = the length of the array<p hidden>53</p>
_Z1fPKii:<p hidden>54</p>
      mov  <input class='' name='Q_6_1' type='text' id='Q_6_1' size='3'/>, #0<p hidden>55</p>
1:<p hidden>56</p>
      <input class='' name='Q_6_2' type='text' id='Q_6_2' size='3'/>  r2, [<input class='' name='Q_6_3' type='text' id='Q_6_3' size='6'/>]<p hidden>57</p>
      add  r0, #<input class='' name='Q_6_4' type='text' id='Q_6_4' size='6'/><p hidden>58</p>
      add  <input class='' name='Q_6_5' type='text' id='Q_6_5' size='3'/>, r2        @add each number onto running total<p hidden>59</p>
      <input class='' name='Q_6_6' type='text' id='Q_6_6' size='4'/> r1, #<input class='' name='Q_6_7' type='text' id='Q_6_7' size='6'/>   @count down to zero<p hidden>60</p>
      bne  <input class='' name='Q_6_8' type='text' id='Q_6_8' size='6'/><p hidden>61</p>
      mov    <input class='' name='Q_6_9' type='text' id='Q_6_9' size='3'/>, r3<p hidden>62</p>
      bx   <input class='' name='Q_6_10' type='text' id='Q_6_10' size='6'/><p hidden>63</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
Complete the following code so that it sums the array of integers at r0<p hidden>51</p>
      @ r0 points to an array of integers<p hidden>52</p>
      @ r1 = the length of the array<p hidden>53</p>
_Z1fPKii:<p hidden>54</p>
      mov  <input class='' name='aQ_6_1' type='text' id='Q_6_1' size='3'/>, #0<p hidden>55</p>
1:<p hidden>56</p>
      <input class='' name='aQ_6_2' type='text' id='Q_6_2' size='3'/>  r2, [<input class='' name='aQ_6_3' type='text' id='Q_6_3' size='6'/>]<p hidden>57</p>
      add  r0, #<input class='' name='aQ_6_4' type='text' id='Q_6_4' size='6'/><p hidden>58</p>
      add  <input class='' name='aQ_6_5' type='text' id='Q_6_5' size='3'/>, r2        @add each number onto running total<p hidden>59</p>
      <input class='' name='aQ_6_6' type='text' id='Q_6_6' size='4'/> r1, #<input class='' name='aQ_6_7' type='text' id='Q_6_7' size='6'/>   @count down to zero<p hidden>60</p>
      bne  <input class='' name='aQ_6_8' type='text' id='Q_6_8' size='6'/><p hidden>61</p>
      mov    <input class='' name='aQ_6_9' type='text' id='Q_6_9' size='3'/>, r3<p hidden>62</p>
      bx   <input class='' name='aQ_6_10' type='text' id='Q_6_10' size='6'/><p hidden>63</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q7'>
      <div>
        7.	checksum: sum array of bytes
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
Complete the following code so that it sums the array of bytes at r0<p hidden>66</p>
      @ r0 points to an array of ***char***<p hidden>67</p>
      @ r1 = the length of the array<p hidden>68</p>
_Z8checksumPKci:<p hidden>69</p>
      mov  r3, #<input class='' name='Q_7_1' type='text' id='Q_7_1' size='6'/><p hidden>70</p>
1:<p hidden>71</p>
      <input class='' name='Q_7_2' type='text' id='Q_7_2' size='3'/>  r2, <input class='' name='Q_7_3' type='text' id='Q_7_3' size='6'/><p hidden>72</p>
      <input class='' name='Q_7_4' type='text' id='Q_7_4' size='3'/>   <input class='' name='Q_7_5' type='text' id='Q_7_5' size='3'/>, r2  @add each letter onto the sum<p hidden>73</p>
      <input class='' name='Q_7_6' type='text' id='Q_7_6' size='3'/>  r0, #<input class='' name='Q_7_7' type='text' id='Q_7_7' size='6'/><p hidden>74</p>
      <input class='' name='Q_7_8' type='text' id='Q_7_8' size='4'/> r1, #<input class='' name='Q_7_9' type='text' id='Q_7_9' size='6'/><p hidden>75</p>
      bne  <input class='' name='Q_7_10' type='text' id='Q_7_10' size='6'/><p hidden>76</p>
      <input class='' name='s_7_11' type='text' id='s_7_11' size='20'/><p hidden>77</p>
      <input class='' name='s_7_12' type='text' id='s_7_12' size='20'/><p hidden>78</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
Complete the following code so that it sums the array of bytes at r0<p hidden>66</p>
      @ r0 points to an array of ***char***<p hidden>67</p>
      @ r1 = the length of the array<p hidden>68</p>
_Z8checksumPKci:<p hidden>69</p>
      mov  r3, #<input class='' name='aQ_7_1' type='text' id='Q_7_1' size='6'/><p hidden>70</p>
1:<p hidden>71</p>
      <input class='' name='aQ_7_2' type='text' id='Q_7_2' size='3'/>  r2, <input class='' name='aQ_7_3' type='text' id='Q_7_3' size='6'/><p hidden>72</p>
      <input class='' name='aQ_7_4' type='text' id='Q_7_4' size='3'/>   <input class='' name='aQ_7_5' type='text' id='Q_7_5' size='3'/>, r2  @add each letter onto the sum<p hidden>73</p>
      <input class='' name='aQ_7_6' type='text' id='Q_7_6' size='3'/>  r0, #<input class='' name='aQ_7_7' type='text' id='Q_7_7' size='6'/><p hidden>74</p>
      <input class='' name='aQ_7_8' type='text' id='Q_7_8' size='4'/> r1, #<input class='' name='aQ_7_9' type='text' id='Q_7_9' size='6'/><p hidden>75</p>
      bne  <input class='' name='aQ_7_10' type='text' id='Q_7_10' size='6'/><p hidden>76</p>
      <input class='' name='as_7_11' type='text' id='s_7_11' size='20'/><p hidden>77</p>
      <input class='' name='as_7_12' type='text' id='s_7_12' size='20'/><p hidden>78</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q8'>
      <div>
        8.	Write an ARM assembler function
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='code'>
Write an ARM function that takes two integer parameters r0 and r1<p hidden>81</p>
and returns r0*r0 - r1*r1<p hidden>82</p>
<textarea rows='10' cols='60' name='T_8_1' id='T_8_1'>-type your code here-</textarea><p hidden>83</p></pre>
    </div>

    <div class='answer'>
      <pre class='code'>
Write an ARM function that takes two integer parameters r0 and r1<p hidden>81</p>
and returns r0*r0 - r1*r1<p hidden>82</p>
<textarea rows='10' cols='60' name='aT_8_1' id='T_8_1'>-type your code here-</textarea><p hidden>83</p></pre>
    </div>
  </div>
  
  
    <div class='footer'>
      <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>
      <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
    </div>
    </form>
</body>
</html>
      