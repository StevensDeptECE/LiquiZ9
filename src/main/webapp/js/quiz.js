var end;
var audioOn = false;
var timeLimit = 0; // minutes
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
  console.log(name + ", " + pledge);
  var submission = [];
  var f = document.forms[0];
  for (var i = 0; i < f.elements.length; i++) {
    submission.push([f.elements[i].id, f.elements[i].value]);
  }
  var json = new XMLHttpRequest();
  json.open("post", "/liquiz/submitQuiz?quizId="+f.getAttribute("data-quizId")+"&custom_canvas_assignment_id="+encodeURI(f.custom_canvas_assignment_id.value)+"&custom_canvas_course_id="+encodeURI(f.custom_canvas_course_id.value), true);
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
  xobj.open("GET", "/liquiz/getAnswers", true);
  xobj.onreadystatechange = function () {
    if (xobj.readyState == 4 && xobj.status == "200") {
      callback(xobj.responseText);
    }
  };
  xobj.send(null);
}

function getJSON(url, callback) {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", url, true);
  xhr.responseType = "json";
  xhr.onload = function () {
    var status = xhr.status;
    if (status === 200) {
      callback(null, xhr.response);
    } else {
      callback(status, xhr.response);
    }
  };
  xhr.send();
}

function showResult() {
  var answerSections = document.getElementsByClassName("answer");
  for (var i = 0; i < answerSections.length; i++) {
    answerSections[i].style.display = "block";
    // answerSections[i].style.color = "#ff0000";
  }

  var inputs = document.getElementsByTagName("input");
  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].checked === true) {
      inputs[i].disabled = false;
    } else {
      inputs[i].readOnly = true;
      inputs[i].disabled = true;
    }
    if (inputs[i].type === "checkbox") {
      inputs[i].style.borderColor = "#ff0000";
    }
  }

  getJSON("/liquiz//getAnswers", function (
    err,
    data
  ) {
    if (err !== null) {
      alert("Something went wrong: " + err);
    } else {
      var answerObj = data;
      var questionAmount = Object.keys(answerObj).length;
      var sectionCurrent, sectionLast = answerObj[0]["id"].split("_")[1];
      var pointsTotal = 0;
      var pointsEarned = 0;

      for (var i = 0; i < questionAmount; i++) {
        var qID = "a" + answerObj[i]["id"];
        var answer = answerObj[i]["answers"];
        var options = document.getElementsByName(qID);
        sectionCurrent = answerObj[i]["id"].split("_")[1];

        if (sectionCurrent != sectionLast) {
          var section = document.getElementById(sectionLast);
          section.innerHTML =
            "You earned: " +
            Math.ceil(100 * pointsEarned) / 100 +
            " points out of: " +
            Math.ceil(100 * pointsTotal) / 100 +
            " points.";

          pointsTotal = 0;
          pointsEarned = 0;
          sectionLast = sectionCurrent;
        }
        pointsTotal += answerObj[i]["pointsT"];
        pointsEarned += answerObj[i]["pointsE"];

        for (var j = 0; j < options.length; j++) {
          options[j].setAttribute("disabled", true);
          if (answer.includes(options[j].value)) {
            if (options[j].type === "radio") {
              options[j].checked = true;
              options[j].disabled = false;
            } else if (options[j].type === "checkbox") {
              options[j].checked = true;
            } else {
              options[j].diabled = false;
              options[j].value = answer;
            }
          }
          options[j].value = answer;
        }
      }
    }
  });
}
