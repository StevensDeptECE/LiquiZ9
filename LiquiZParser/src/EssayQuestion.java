public class EssayQuestion extends Question {
    private String defaultText;
    public void buildHTML(StringBuilder b) {
        b.append("<textarea name='").append(qId).append("'>\n").append(defaultText).append("</textarea>\n");
    }

    public void buildAnswers(StringBuilder b) {
        //TODO:
    }
    public EssayQuestion(int questionNumber, int partNumber, String defaultText) {
        super(questionNumber, partNumber);
        this.defaultText = defaultText;
    }
}
