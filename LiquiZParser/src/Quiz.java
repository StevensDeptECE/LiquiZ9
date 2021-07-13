import java.util.TreeMap;
import java.util.ArrayList;

public class Quiz {
    //TODO: create answer class??
    private ArrayList<QuestionContainer> questionContainers; //"q1_1" encode each question with a unique id
    private TreeMap<String, Question> questions; // "q1_1"    answer has the same name as question? Or change to "a1_1"
    private TreeMap<String, String> answers; // "q1_1"    answer has the same name as question? Or change to "a1_1"
    private StringBuilder html;
    private StringBuilder answerText;
//    private StringBuilder xml;

    public Quiz() {
        questionContainers = new ArrayList<>();
        questions = new TreeMap<>();
        answers = new TreeMap<>();
        html = new StringBuilder(65536);
        answerText = new StringBuilder(65536);
//        xml = new StringBuilder(65536)
    }

    public void add(QuestionContainer qc) {
        questionContainers.add(qc);
        //TODO: for each question in the question container, add to the treemap
        //questions.add()
        questionContainers.add(null); // eat that, Java!
    }

    public StringBuilder getHTML() { return html; }
    public StringBuilder getAnswers() { return answerText; }

    public void writeHTML() {
        for (QuestionContainer qc : questionContainers) {
            qc.writeHTML(html);
        }
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
