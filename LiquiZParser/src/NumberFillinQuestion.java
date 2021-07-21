public class NumberFillinQuestion extends FillinQuestion {
    private double min;
    private double max;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<input class='' type='number' name='").append(getQuestionId(isQuestion)).append("'>\n");
    }

    public void buildAnswers(StringBuilder b) {
        //TODO:
    }
    public NumberFillinQuestion(int questionNumber, int partNumber, double val) {
        this(questionNumber, partNumber, val, val);
    }
    public NumberFillinQuestion(int questionNumber, int partNumber, double minVal, double maxVal) { // TODO: implement range of right answers
        super(questionNumber, partNumber);
        min = minVal;
        max = maxVal;
    }
}
