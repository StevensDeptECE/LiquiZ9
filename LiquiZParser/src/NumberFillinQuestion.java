public class NumberFillinQuestion extends Question {
    private double min;
    private double max;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<input class='input' type='number' name='")
                .append(getQuestionId(isQuestion)).append("'>\n");
    }

    public void buildAnswers(StringBuilder b) {
        //TODO: how to get points for question in container
        b.append(getPoints()).append("\t")
                .append(min).append(",").append(max).append("\n");
    }
    public NumberFillinQuestion(int questionNumber, int partNumber, double points, double val) {
        this(questionNumber, partNumber, points, val, val);
    }
    public NumberFillinQuestion(int questionNumber, int partNumber, double points, double minVal, double maxVal) { // TODO: implement range of right answers
        super(questionNumber, partNumber, points);
        min = minVal;
        max = maxVal;
    }
}
