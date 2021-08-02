import java.io.FileWriter;
import java.io.PrintWriter;

public class TestQuiz {
  public static void main(String[] args) throws Exception {
    Quiz quiz = new Quiz();

    QuestionContainer qc = new QuestionContainer((float)10.0, "Enter a number");
    qc.add(new Text("What is 2+2?"));
    qc.add(new NumberFillinQuestion(1, 1, 4));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Text("Where is Stevens?"));
    qc.add(new FillinQuestion(2, 1, "Hoboken"));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new EssayQuestion(3, 1, "default text goes here. Type something!"));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new Instructions(
        "After watching the video, explain how the bridge failed"));
    qc.add(new Video("TacomaNarrowsBridge.mp4"));
    qc.add(new EssayQuestion(4, 1, "type your essay here"));
    quiz.add(qc);

    qc = new QuestionContainer();
    String[] vMCAns = {"1st Ans", "2nd Ans", "3rd Ans"};
    qc.add(new VerticalMultipleChoiceQuestion(5, 1, vMCAns, vMCAns));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Instructions("choose one"));
    qc.add(new HorizontalMultipleChoiceQuestion(6, 1, true, vMCAns, vMCAns));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Audio("over.ogg"));
    quiz.add(qc);

    quiz.writeHTML();
    System.out.println(quiz.getHTML());
    PrintWriter pw = new PrintWriter(new FileWriter("Quiz.html"));
    pw.println(quiz.getHTML());
    pw.close();
  }
}
