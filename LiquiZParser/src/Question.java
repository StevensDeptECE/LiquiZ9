public abstract class Question extends DisplayElement {
    protected String qId;
    protected double points;
    public String getQuestionId(boolean isQuestion) { return isQuestion ? qId : "a" + qId; }
    public abstract void buildHTML(StringBuilder b, boolean isQuestion);
    public abstract void buildAnswers(StringBuilder b);
    public abstract String getqIdType();

    public final void writeEnclosingDiv(StringBuilder b) {
        b.append("<div>");
    }

    public final void writeHTML(StringBuilder b, boolean isQuestion) {
        writeEnclosingDiv(b);
        buildHTML(b, isQuestion);
        b.append("</div>");
    }
    public final void writeAnswers(StringBuilder b) {
        b.append(qId).append("\t");
        buildAnswers(b);
    }
    public final double getPoints() {
        return points;
    }
    public Question(int questionNumber, int partNumber) {
        qId = getqIdType() + questionNumber + "_" + partNumber; //TODO change first letter to match Ethan's question code
        points = 0.0;
    }
    public Question(int questionNumber, int partNumber, double points) {
        qId = getqIdType() + questionNumber + "_" + partNumber; //TODO change first letter to match Ethan's question code
        this.points = points;
    }

}
