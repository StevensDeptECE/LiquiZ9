public class VerticalMultipleChoiceQuestion extends MultipleChoiceQuestion {
    public void buildHTML(StringBuilder b) {
        b.append("<table>\n");
        for (int i = 0; i < answers.length; i++) {
            b.append("<tr><td>").append(answers[i]).append("</td></tr>\n"); //TODO: maket his decent format! displays all answers
        }
        b.append("</table>\n");
    }
}
