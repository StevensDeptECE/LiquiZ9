var end;
var audioOn = false;
var timeLimit = 30; // minutes
var firstAlert = 29.5;
var alertInterval = 0.166666;
var nextAlert = firstAlert;
var countDownThread;

function scheduleAudio() {
  audioOn = !audioOn;
  document.getElementById("audioControl").value = audioOn
    ? "turn audio alerts OFF"
    : "turn audio alerts ON";
}

function updateControls() {
  var d = new Date();
  var remaining = end - d.getTime();
  if (remaining < 0) clearInterval(countDownThread);

  if (audioOn && remaining <= nextAlert * 60000) {
    var audioAlertName =
      nextAlert >= firstAlert
        ? "alert25"
        : nextAlert >= firstAlert - alertInterval
        ? "alert20"
        : nextAlert >= firstAlert - alertInterval * 2
        ? "alert15"
        : nextAlert >= firstAlert - alertInterval * 3
        ? "alert10"
        : nextAlert >= firstAlert - alertInterval * 4
        ? "alert5"
        : nextAlert >= firstAlert - alertInterval * 5
        ? "alertover"
        : null;

    nextAlert -= alertInterval;
    if (audioAlertName != null) document.getElementById(audioAlertName).play();
  }
  var min = Math.floor(remaining / 60000);
  var seconds = Math.floor((remaining - min * 60000) / 1000);
  if (seconds < 10) seconds = "0" + seconds;
  var timeDisp = min + ":" + seconds;
  document.getElementById("topTime").innerText = timeDisp;
  document.getElementById("bottomTime").innerText = timeDisp;
}

function startTime(limit) {
  end = new Date().getTime() + limit * 60000;
  updateControls();
  countDownThread = setInterval(updateControls, 2000);
}

function checkAndSubmit() {
  var pledge = document.getElementById("pledge");
  console.log(userid + ", " + passwd);
  console.log(name + ", " + pledge);
  var submission = [];
  var f = document.forms[0];
  for (var i = 0; i < f.elements.length; i++)
    submission.push([f.elements[i].id, f.elements[i].value]);
  console.log(submission);
  var json = new XMLHttpRequest();
  json.open("post", "gradquiz.jsp", true);
  json.setRequestHeader("Content-Type", "application/json");
  // Create a state change callback
  json.onreadystatechange = function () {
    if (json.readyState === 4 && json.status === 200) {
      // Print received data from server
      document.body.innerHTML = this.responseText;
    }
  };
  json.send(JSON.stringify(submission));
  //    document.forms[0].submit();
}

function selectMusic(v) {
  document.getElementById(v).play();
}

function protestRequest() {
  window.open(
    "protest-window.html",
    "Ratting",
    "width=550,height=500,left=150,top=200,toolbar=0,status=0"
  );
}

function loadJSON(callback) {
  var xobj = new XMLHttpRequest();
  xobj.overrideMimeType("application/json");
  xobj.open("GET", "answers.json", true);
  xobj.onreadystatechange = function () {
    if (xobj.readyState == 4 && xobj.status == "200") {
      callback(xobj.responseText);
    }
  };
  xobj.send(null);
}

//var answerObj = JSON.parse(
  //'{"1":{"points earned":"10", "answer":"cat"},"2":{"points earned":"10", "answer":"Kruger"},"3":{"points earned":"10", "answer":"Kruger, Favardin,Song, Lu"},"4":{"points earned":"10", "asnwer":"Hoboken, United States, New Jersey"},"5":{"points earned":"10", "answer":"N/A"},"6":{"points earned":"10", "ansswer":"pc, sp, lr, share the same bits"},"7":{"points earned":"10", "answer":"N/A"},"8":{"points earned":"10", "answer":"O(log(n)), b, gcd"},"9":{"points earned":"10", "answer":"00000000, 000102b8, ffffffff, 000102bc, 00000000, 1, 1, 0, 00000000, 0, 0, 0, ffffffff, 1, 0, 1"},"10":{"points earned":"10", "answer":"AND"},"11":{"points earned":"10", "answer":"N/A"},"12":{"points earned":"10", "answer":"kind of, instance of, instantiate, instantiation, encapsulation"}}'
//);

function showResult() {
  loadJSON(function(response) {
       var answerObj = JSON.parse(response);
  });
  var questionAmount = Object.keys(answerObj).length;
  for (var questionCount = 1; questionCount <= questionAmount; questionCount++) {
    var x = document.getElementById(questionCount);
    x.innerHTML = "You earned " + answerObj[questionCount]["points earned"] + " points. The answer(s) are: " + answerObj[questionCount].answer;
  }
}