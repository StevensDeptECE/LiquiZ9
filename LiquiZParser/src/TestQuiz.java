import java.io.FileWriter;
import java.io.PrintWriter;

public class TestQuiz {
  public static void main(String[] args) throws Exception {
    Quiz quiz = new Quiz();
    Preferences prefs = new Preferences();

    QuestionContainer qc = new QuestionContainer((float)10.0, "Enter a number");
    qc.add(new Text("What is 2+2?"));
    qc.add(new NumberFillinQuestion(1, 1, 10.0, 4));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Text("Where is Stevens?"));
    qc.add(new FillinQuestion(2, 1, 5.0, "Hoboken"));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new EssayQuestion(3, 1, 7.0, "default text goes here. Type something!", "Answer Here"));
    quiz.add(qc);

    qc = new QuestionContainer();
    // qc.add(new Instructions("Watch the following video, then explain why the
    // bridge fell down")); c.add(new Video("whatever.mpg"));
    qc.add(new Instructions(
        "After watching the video, explain how the bridge failed"));
    qc.add(new Video("TacomaNarrowsBridge.mp4"));
    qc.add(new EssayQuestion(4, 1, 6.0, "type your essay here", "Answer Here"));
    quiz.add(qc);

    qc = new QuestionContainer();
    String vMCAns = "1st Ans,2nd Ans,*3rd Ans";
    qc.add(new VerticalMultipleChoiceQuestion(5, 1, 5.5, vMCAns));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Instructions("choose one"));
    qc.add(new HorizontalMultipleChoiceQuestion(6, 1, 3.2, vMCAns, true));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(new Audio("over.ogg"));
    quiz.add(qc);

    qc = new QuestionContainer();
    //qc.add(new MatrixQuestion(7, 1, 10.0, 2, 3, "1,2,3,4,5,_6_7"));
    qc.add(new Instructions("Create a 2x2 identity matrix"));
    //qc.add(new MatrixQuestion(7, 2, 10.0, 2, 2, "_,0,_,_"));
    quiz.add(qc);

    qc = new QuestionContainer();
    qc.add(HorizontalMultipleChoiceQuestion.lookUpPreDefined(8, 1, 3.5, prefs, "I like ice cream?", "likert5"));
    quiz.add(qc);//TODO: add survey -> no right answer

    quiz.writeHTML();
    quiz.writeAnswers();
    System.out.println(quiz.getHTML());
    System.out.println(quiz.getAnswers());
    PrintWriter pw = new PrintWriter(new FileWriter("Quiz.html"));
    pw.println(quiz.getHTML());
    pw.close();
  }
}
