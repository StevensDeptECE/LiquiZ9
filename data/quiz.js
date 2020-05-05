var end;
var audioOn = false;
var timeLimit = 30; // minutes
var firstAlert = 29.5;
var alertInterval = 0.166666;
var nextAlert = firstAlert;
var countDownThread;
function scheduleAudio() {
    audioOn = !audioOn;
    document.getElementById("audioControl").value
	=audioOn ? "turn audio alerts OFF" : "turn audio alerts ON";
}

function updateControls() {
    var d = new Date();
    var remaining = end - d.getTime();
    if (remaining < 0)
	clearInterval(countDownThread);
//    console.log(remaining);
    if (audioOn && remaining <= nextAlert*60000) {
	var audioAlertName = (nextAlert >= firstAlert ? 'alert25' :
	    (nextAlert>=firstAlert-alertInterval ? 'alert20' :
	     (nextAlert>=firstAlert-alertInterval*2 ? 'alert15' :
	      (nextAlert>=firstAlert-alertInterval*3 ? 'alert10' :
	       (nextAlert>=firstAlert-alertInterval*4 ? 'alert5' :
		(nextAlert>=firstAlert-alertInterval*5 ? 'alertover' : null))))));
//	console.log(nextAlert);
//	console.log(audioAlertName);
	nextAlert-=alertInterval;
	if (audioAlertName != null)
  	  document.getElementById(audioAlertName).play();
    }
    var min = Math.floor(remaining / 60000);
    var seconds = Math.floor((remaining - min * 60000) / 1000);
    if (seconds < 10)
	seconds = '0' + seconds;
    var timeDisp = min + ":" + seconds;
    document.getElementById("topTime").innerText = timeDisp;
    document.getElementById("bottomTime").innerText = timeDisp;
}

function startTime() {
    end = new Date().getTime() + 30 * 60000;
    updateControls();
    countDownThread = setInterval(updateControls, 2000);
}

function checkAndSubmit() {
    var userid = document.getElementById("userid");
    var passwd = document.getElementById("passwd");
    var name = document.getElementById("name");
    var pledge = document.getElementById("pledge");
    console.log(userid + ", " + passwd);
    console.log(name + ", " + pledge);
    var submission = [];
    var f = forms[0];
    for (var i = 0; i < f.elements.length; i++)
	submission.push([f.elements[i].id, f.elements[i].value]);
    console.log(submission);
    document.forms[0].submit();
}

function selectMusic(v) {
    document.getElementById(v).play();
}
