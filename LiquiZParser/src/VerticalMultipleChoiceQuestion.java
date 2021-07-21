public class VerticalMultipleChoiceQuestion extends MultipleChoiceQuestion {
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<table>\n");
        for (int i = 0; i < answers.length; i++) {
            b.append("<tr><td>").append(answers[i]).append("</td></tr>\n"); //TODO: make this decent format! displays all answers
        }
        b.append("</table>\n");
    }
    public VerticalMultipleChoiceQuestion(int questionNumber, int partNumber) {
        super(questionNumber, partNumber);
    }
}
