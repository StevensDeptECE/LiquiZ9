public class EssayQuestion extends Question {
    private String defaultText;
    private String answer;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<textarea name='").append(getQuestionId(isQuestion)).append("'>\n")
                .append(defaultText)
                .append("</textarea>\n");
    }
    public void buildAnswers(StringBuilder b) {
        b.append(getPoints()).append("\t").append(answer).append("\n");
    }
    public String getqIdType() {
        return "q_";
    }
    public EssayQuestion(int questionNumber, int partNumber, double points, String defaultText, String answer) {
        super(questionNumber, partNumber, points);
        this.defaultText = defaultText;
        this.answer = answer;
    }
}
