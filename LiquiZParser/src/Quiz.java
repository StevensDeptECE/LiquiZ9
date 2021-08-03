import java.util.ArrayList;
import java.util.TreeMap;

public class Quiz {
  // TODO: create answer class??
  private ArrayList<QuestionContainer>
      questionContainers; //"q1_1" encode each question with a unique id
  private TreeMap<String, Question>
      questions; // "q1_1"    answer has the same name as question? Or change to
                 // "a1_1"
  private TreeMap<String, String> answers; // "q1_1"    answer has the same name
                                           // as question? Or change to "a1_1"
  private StringBuilder html;
  private StringBuilder answerText;
  private String name;
  private double points;
  private Preferences pref;
  //    private StringBuilder xml;

  public Quiz() {
    questionContainers = new ArrayList<>();
    questions = new TreeMap<>();
    answers = new TreeMap<>();
    html = new StringBuilder(65536);
    answerText = new StringBuilder(65536);
    name = "TODO";
    points = 0.0;
    //        xml = new StringBuilder(65536)
  }

  public void add(QuestionContainer qc) {
    questionContainers.add(qc);
    points+=qc.getPoints();
    // TODO: for each question in the question container, add to the treemap
    // questions.add()
  }

  public StringBuilder getHTML() { return html; }
  public StringBuilder getAnswers() { return answerText; }

  public void writeHTML() {
    html.append("<!DOCTYPE html>\n")
        .append("<html>\n<head>\n<link rel='stylesheet' type='text/css' href='css/quiz.css'>\n")
        .append("<script src='js/quiz.js'></script>\n")
        .append("\n<title>")
        .append(name)
        .append("</title>\n</head>\n\n\n<body>\n")
        .append(
                "<form method='post' action='submitQuiz'>\n"
            + "<div id='header' class='header'>\n"
            + "    <img class='logo' src='media/StevensLogo.png'/>\n"
            + "    <div class='headerText'>\n"
            + "      <div class='quizTitle'>\n"
            + "        Loops\n"
            + "      </div>\n"
            + "\n"
            + "      <div class='headerDetails'>\n"
            + "        <div class='headerRow'>\n"
            + "          \n"
            + "        </div>\n"
            + "        <div class='headerRow'>\n"
            + "          Email    if you have any questions!\n"
            + "        </div>\n"
            + "        <div class='headerRow'>\n"
            +
            "          <input id='pledge' type='checkbox' name='pledged' value='pledged'/>\n"
            +
            "          <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>\n"
            + "        </div>\n"
            +
            "        <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>\n"
            +
            "        <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>\n"
            + "      </div>\n"
            + "    </div>\n"
            +
            "    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>\n"
            + "  </div>\n\n");

    int questionNumber = 1;
    for (QuestionContainer qc : questionContainers) {
      qc.writeHTML(html, questionNumber);
      questionNumber++;
    }
    html.append(
            "    <div class='footer'>\n"
            + "      <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>\n"
            + "      <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>\n"
            + "    </div>"
            + "</form>\n</body>\n</html>\n");
    //     while (Entry<...> q = questions.next()) {
    //         String qId = q.key(); QuestionContainer q = q.value();
    //        q.writeHTML(html);
    //    }
  }
  public void writeAnswers() {
    for(QuestionContainer qc : questionContainers) {
      qc.writeAnswers(answerText);
    }
    //  while (Entry<...> q = questions.next()) {
    //      String qId = q.key(); Question q = q.value();
    //      q.writeAnswers(answerText);
    //  }
  }
  /*
  public void buildXML() {
      while (Entry<...> q = questions.next()) {
          String qId = q.key(); Question q = q.value();
          q.buildXML(answerText);
      }
  }
  */
}
