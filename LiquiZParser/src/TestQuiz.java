import java.io.FileWriter;
import java.io.PrintWriter;

public class TestQuiz {
  public static void main(String[] args) throws Exception {
    Quiz quiz = new Quiz();

    QuestionContainer qc = new QuestionContainer();
    qc.add(new Text("What is 2+2?"));
    qc.add(new NumberFillinQuestion(1, 1, 4));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new EssayQuestion(2, 1, "default text goes here. Type something!"));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new Instructions(
        "After watching the video, explain how the bridge failed"));
    qc.add(new Video("TacomaNarrowsBridge.mp4"));
    qc.add(new EssayQuestion(3, 1, "type your essay here"));
    quiz.add(qc);

    quiz.writeHTML();
    System.out.println(quiz.getHTML());
    PrintWriter pw = new PrintWriter(new FileWriter("Quiz.html"));
    pw.println(quiz.getHTML());
    pw.close();
  }
}
