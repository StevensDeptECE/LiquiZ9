
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <link rel="stylesheet" type="text/css" href='media/quiz.css'>
  <title>
    LiQuiz [Memory Alignment]
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
        Memory Alignment
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
        1.	Sizes
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='pcode'>
State the value of the following sizes on a Raspberry Pi<p hidden>3</p>
<p hidden>4</p>
sizeof(int)        <input class='' name='q_1_1' type='text' id='q_1_1' size='6'/><p hidden>5</p>
sizeof(uint32_t)   <input class='' name='q_1_2' type='text' id='q_1_2' size='6'/><p hidden>6</p>
uint32_t x;<p hidden>7</p>
sizeof(char)       <input class='' name='q_1_3' type='text' id='q_1_3' size='6'/><p hidden>8</p>
sizeof(x)          <input class='' name='q_1_4' type='text' id='q_1_4' size='6'/><p hidden>9</p>
sizeof(double)     <input class='' name='q_1_5' type='text' id='q_1_5' size='6'/><p hidden>10</p>
sizeof(uint8_t)    <input class='' name='q_1_6' type='text' id='q_1_6' size='6'/><p hidden>11</p>
sizeof(uint64_t)   <input class='' name='q_1_7' type='text' id='q_1_7' size='6'/><p hidden>12</p>
sizeof(uint16_t)   <input class='' name='q_1_8' type='text' id='q_1_8' size='6'/><p hidden>13</p>
uint32_t a[10]<p hidden>14</p>
sizeof(a)          <input class='' name='q_1_9' type='text' id='q_1_9' size='6'/><p hidden>15</p>
double b[20];<p hidden>16</p>
sizeof(b)          <input class='' name='q_1_10' type='text' id='q_1_10' size='6'/><p hidden>17</p>
uint32_t c[3][5];<p hidden>18</p>
sizeof(c)          <input class='' name='q_1_11' type='text' id='q_1_11' size='6'/><p hidden>19</p></pre>
    </div>

    <div class='answer'>
      <pre class='pcode'>
State the value of the following sizes on a Raspberry Pi<p hidden>3</p>
<p hidden>4</p>
sizeof(int)        <input class='' name='aq_1_1' type='text' id='q_1_1' size='6'/><p hidden>5</p>
sizeof(uint32_t)   <input class='' name='aq_1_2' type='text' id='q_1_2' size='6'/><p hidden>6</p>
uint32_t x;<p hidden>7</p>
sizeof(char)       <input class='' name='aq_1_3' type='text' id='q_1_3' size='6'/><p hidden>8</p>
sizeof(x)          <input class='' name='aq_1_4' type='text' id='q_1_4' size='6'/><p hidden>9</p>
sizeof(double)     <input class='' name='aq_1_5' type='text' id='q_1_5' size='6'/><p hidden>10</p>
sizeof(uint8_t)    <input class='' name='aq_1_6' type='text' id='q_1_6' size='6'/><p hidden>11</p>
sizeof(uint64_t)   <input class='' name='aq_1_7' type='text' id='q_1_7' size='6'/><p hidden>12</p>
sizeof(uint16_t)   <input class='' name='aq_1_8' type='text' id='q_1_8' size='6'/><p hidden>13</p>
uint32_t a[10]<p hidden>14</p>
sizeof(a)          <input class='' name='aq_1_9' type='text' id='q_1_9' size='6'/><p hidden>15</p>
double b[20];<p hidden>16</p>
sizeof(b)          <input class='' name='aq_1_10' type='text' id='q_1_10' size='6'/><p hidden>17</p>
uint32_t c[3][5];<p hidden>18</p>
sizeof(c)          <input class='' name='aq_1_11' type='text' id='q_1_11' size='6'/><p hidden>19</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q2'>
      <div>
        2.	structure size
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='pcode'>
Identify the size of the structures<p hidden>22</p>
struct A {<p hidden>23</p>
  uint32_t a;<p hidden>24</p>
  char     b;<p hidden>25</p>
  uint32_t c;<p hidden>26</p>
  uint16_t d;<p hidden>27</p>
  char     e;<p hidden>28</p>
};<p hidden>29</p>
sizeof(A)      <input class='' name='q_2_1' type='text' id='q_2_1' size='6'/><p hidden>30</p>
<p hidden>31</p>
struct B {<p hidden>32</p>
  uint32_t a;<p hidden>33</p>
  uint32_t b;<p hidden>34</p>
  uint16_t c;<p hidden>35</p>
  char     d;<p hidden>36</p>
  char     e;<p hidden>37</p>
};<p hidden>38</p>
sizeof(B)      <input class='' name='q_2_2' type='text' id='q_2_2' size='6'/><p hidden>39</p>
<p hidden>40</p>
struct C {<p hidden>41</p>
  char     a;<p hidden>42</p>
  double   b;<p hidden>43</p>
  uint32_t c;<p hidden>44</p>
  double   d;<p hidden>45</p>
  char     e;<p hidden>46</p>
};<p hidden>47</p>
sizeof(C)      <input class='' name='q_2_3' type='text' id='q_2_3' size='6'/><p hidden>48</p>
<p hidden>49</p>
struct D {<p hidden>50</p>
  double   a;<p hidden>51</p>
  double   b;<p hidden>52</p>
  uint32_t c;<p hidden>53</p>
  char     d;<p hidden>54</p>
  char     e;<p hidden>55</p>
};<p hidden>56</p>
sizeof(D)      <input class='' name='q_2_4' type='text' id='q_2_4' size='6'/><p hidden>57</p></pre>
    </div>

    <div class='answer'>
      <pre class='pcode'>
Identify the size of the structures<p hidden>22</p>
struct A {<p hidden>23</p>
  uint32_t a;<p hidden>24</p>
  char     b;<p hidden>25</p>
  uint32_t c;<p hidden>26</p>
  uint16_t d;<p hidden>27</p>
  char     e;<p hidden>28</p>
};<p hidden>29</p>
sizeof(A)      <input class='' name='aq_2_1' type='text' id='q_2_1' size='6'/><p hidden>30</p>
<p hidden>31</p>
struct B {<p hidden>32</p>
  uint32_t a;<p hidden>33</p>
  uint32_t b;<p hidden>34</p>
  uint16_t c;<p hidden>35</p>
  char     d;<p hidden>36</p>
  char     e;<p hidden>37</p>
};<p hidden>38</p>
sizeof(B)      <input class='' name='aq_2_2' type='text' id='q_2_2' size='6'/><p hidden>39</p>
<p hidden>40</p>
struct C {<p hidden>41</p>
  char     a;<p hidden>42</p>
  double   b;<p hidden>43</p>
  uint32_t c;<p hidden>44</p>
  double   d;<p hidden>45</p>
  char     e;<p hidden>46</p>
};<p hidden>47</p>
sizeof(C)      <input class='' name='aq_2_3' type='text' id='q_2_3' size='6'/><p hidden>48</p>
<p hidden>49</p>
struct D {<p hidden>50</p>
  double   a;<p hidden>51</p>
  double   b;<p hidden>52</p>
  uint32_t c;<p hidden>53</p>
  char     d;<p hidden>54</p>
  char     e;<p hidden>55</p>
};<p hidden>56</p>
sizeof(D)      <input class='' name='aq_2_4' type='text' id='q_2_4' size='6'/><p hidden>57</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q3'>
      <div>
        3.	size of arrays
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='pcode'>
Identify each size on a Raspberry pi (32-bit addresses, 64-bit alignment)<p hidden>60</p>
<p hidden>61</p>
void f(uint32_t x[], double y[4]) {<p hidden>62</p>
  // sizeof(x) <input class='' name='q_3_1' type='text' id='q_3_1' size='6'/><p hidden>63</p>
  // sizeof(y) <input class='' name='q_3_2' type='text' id='q_3_2' size='6'/><p hidden>64</p>
}<p hidden>65</p>
<p hidden>66</p>
int main() {<p hidden>67</p>
  uint32_t a[7];<p hidden>68</p>
  // sizeof(a)  <input class='' name='q_3_3' type='text' id='q_3_3' size='6'/><p hidden>69</p>
}<p hidden>70</p></pre>
    </div>

    <div class='answer'>
      <pre class='pcode'>
Identify each size on a Raspberry pi (32-bit addresses, 64-bit alignment)<p hidden>60</p>
<p hidden>61</p>
void f(uint32_t x[], double y[4]) {<p hidden>62</p>
  // sizeof(x) <input class='' name='aq_3_1' type='text' id='q_3_1' size='6'/><p hidden>63</p>
  // sizeof(y) <input class='' name='aq_3_2' type='text' id='q_3_2' size='6'/><p hidden>64</p>
}<p hidden>65</p>
<p hidden>66</p>
int main() {<p hidden>67</p>
  uint32_t a[7];<p hidden>68</p>
  // sizeof(a)  <input class='' name='aq_3_3' type='text' id='q_3_3' size='6'/><p hidden>69</p>
}<p hidden>70</p></pre>
    </div>
  </div>
  
  
  <div class='section'>
    <div class='question' id='q4'>
      <div>
        4.	size of arrays
        <span class='pts'>  (1 points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>
      </div>
      <pre class='pcode'>
Identify each size on Intel (64-bit addresses, 64-bit alignment)<p hidden>73</p>
<p hidden>74</p>
void f(uint32_t x[], double y[4]) {<p hidden>75</p>
  // sizeof(x) <input class='' name='q_4_1' type='text' id='q_4_1' size='6'/><p hidden>76</p>
  // sizeof(y) <input class='' name='q_4_2' type='text' id='q_4_2' size='6'/><p hidden>77</p>
}<p hidden>78</p>
<p hidden>79</p>
int main() {<p hidden>80</p>
  uint32_t a[7];<p hidden>81</p>
  // sizeof(a)  <input class='' name='q_4_3' type='text' id='q_4_3' size='6'/><p hidden>82</p>
  uint32_t* b[5];<p hidden>83</p>
  // sizeof(b)  <input class='' name='q_4_4' type='text' id='q_4_4' size='6'/>  <p hidden>84</p>
}<p hidden>85</p></pre>
    </div>

    <div class='answer'>
      <pre class='pcode'>
Identify each size on Intel (64-bit addresses, 64-bit alignment)<p hidden>73</p>
<p hidden>74</p>
void f(uint32_t x[], double y[4]) {<p hidden>75</p>
  // sizeof(x) <input class='' name='aq_4_1' type='text' id='q_4_1' size='6'/><p hidden>76</p>
  // sizeof(y) <input class='' name='aq_4_2' type='text' id='q_4_2' size='6'/><p hidden>77</p>
}<p hidden>78</p>
<p hidden>79</p>
int main() {<p hidden>80</p>
  uint32_t a[7];<p hidden>81</p>
  // sizeof(a)  <input class='' name='aq_4_3' type='text' id='q_4_3' size='6'/><p hidden>82</p>
  uint32_t* b[5];<p hidden>83</p>
  // sizeof(b)  <input class='' name='aq_4_4' type='text' id='q_4_4' size='6'/>  <p hidden>84</p>
}<p hidden>85</p></pre>
    </div>
  </div>
  
  
    <div class='footer'>
      <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>
      <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
    </div>
    </form>
</body>
</html>
      