package edu.stevens;

public class FillinQuestion extends Question {
    private String answer;
    //TODO: This class has only what all multiplechoice questions have in common
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div><input class='input' type='text' name='")
                .append(getQuestionId(isQuestion)).append("'></div>\n");
    }

    public void buildAnswers(StringBuilder b) {
        b.append(getPoints()).append("\t").append(answer).append("\n");//TODO: how to get points
    }
    public FillinQuestion(int questionNumber, int partNumber, double points, String answer) {
        super(questionNumber, partNumber, points);
        this.answer = answer;
    }

}
