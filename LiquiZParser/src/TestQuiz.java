public class TestQuiz {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();
        QuestionContainer qc = new QuestionContainer();
        qc.add(new Text("What is 2+2?"));
        qc.add(new NumberFillinQuestion(4));
        quiz.add(qc);

        qc = new QuestionContainer();
        qc.add(new Instructions("Watch the following video, then explain why the bridge fell down"));
        qc.add(new Video("whatever.mpg"));
        qc.add(new EssayQuestion("default text goes here. Type something!"));
        quiz.add(qc);

        quiz.writeHTML();
        System.out.println(quiz.getHTML());


    }



}
