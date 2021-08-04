public class DropDownQuestion extends MultipleChoiceQuestion {
    protected String[] choices;
    protected String[] answers;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<select>\n");
        for (int i = 0; i < choices.length; i++) {
            b.append("<option value='").append(choices[i]).append("'>").append(choices[i]).append("</option>\n"); //TODO: make this decent format! displays all answers
        }
        b.append("<select>\n");
    }
    public void buildAnswers(StringBuilder b) {
        b.append("getPoints()").append("\t").append(answers[0]);//TODO: figure out how to get points
        for(int i = 1; i < answers.length; i++) {
            b.append(",").append(answers[i]);
        }
        b.append("\n");
    }
    public String getqIdType() {
        return "q_";
    }
    public DropDownQuestion(int questionNumber, int partNumber, double points, boolean multiAns, String choices, String answers) {
        super(questionNumber, partNumber, points);
        this.choices = choices.split(",");
        this.answers = answers.split(",");
    }
}
