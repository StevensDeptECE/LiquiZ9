import java.util.TreeMap;
import java.util.ArrayList;

public class Quiz {
    //TODO: create answer class??
    private ArrayList<QuestionContainer> questionContainers; //"q1_1" encode each question with a unique id
    private TreeMap<String, Question> questions; // "q1_1"    answer has the same name as question? Or change to "a1_1"
    private TreeMap<String, String> answers; // "q1_1"    answer has the same name as question? Or change to "a1_1"
    private StringBuilder html;
    private StringBuilder answerText;
    private String name;
//    private StringBuilder xml;

    public Quiz() {
        questionContainers = new ArrayList<>();
        questions = new TreeMap<>();
        answers = new TreeMap<>();
        html = new StringBuilder(65536);
        answerText = new StringBuilder(65536);
        name = "TODO";
//        xml = new StringBuilder(65536)
    }

    public void add(QuestionContainer qc) {
        questionContainers.add(qc);
        //TODO: for each question in the question container, add to the treemap
        //questions.add()
    }

    public StringBuilder getHTML() { return html; }
    public StringBuilder getAnswers() { return answerText; }

    public void writeHTML() {
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n<link rel='stylesheet' type='text/css' href='css/quiz.css'>\n<title>").append(name).append("</title>\n</head>\n<body>\n");






        html.append(
        "<div id='header' class='header'>\n" +
                "    <img class='logo' src='media/StevensLogo.png'/>\n" +
                "    <div class='headerText'>\n" +
                "      <div class='quizTitle'>\n" +
                "        Loops\n" +
                "      </div>\n" +
                "\n" +
                "      <div class='headerDetails'>\n" +
                "        <div class='headerRow'>\n" +
                "          \n" +
                "        </div>\n" +
                "        <div class='headerRow'>\n" +
                "          Email    if you have any questions!\n" +
                "        </div>\n" +
                "        <div class='headerRow'>\n" +
                "          <input id='pledge' type='checkbox' name='pledged' value='pledged'/>\n" +
                "          <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>\n" +
                "        </div>\n" +
                "        <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>\n" +
                "        <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>\n" +
                "  </div>");

        for (QuestionContainer qc : questionContainers) {
            qc.writeHTML(html);
        }
        html.append("</body>\n</html>\n");
   //     while (Entry<...> q = questions.next()) {
   //         String qId = q.key(); QuestionContainer q = q.value();
    //        q.writeHTML(html);
    //    }
    }
    public void writeAnswers() {
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
