public class EssayQuestion extends Question {
    private String defaultText;
    private String answer;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<textarea name='").append(getQuestionId(isQuestion)).append("'>\n")
                .append(defaultText)
                .append("</textarea>\n");
    }

    public void buildAnswers(StringBuilder b) {
        b.append("getPoints()").append("\t").append(answer).append("\n");//TODO: how to get points
    }
    public EssayQuestion(int questionNumber, int partNumber, String defaultText, String answer) {
        super(questionNumber, partNumber);
        this.defaultText = defaultText;
        this.answer = answer;
    }
}
