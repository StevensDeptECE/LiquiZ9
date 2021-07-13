public abstract class Question extends DisplayElement {
    protected String qId;

    public abstract void buildHTML(StringBuilder b);
    public abstract void buildAnswers(StringBuilder b);

    public final void writeEnclosingDiv(StringBuilder b) {
        b.append("<div id='").append(qId).append("'>");
    }

    public final void writeHTML(StringBuilder b) {
        writeEnclosingDiv(b);
        buildHTML(b);
        b.append("</div>");
    }
    public final void writeAnswers(StringBuilder b) {
        b.append(qId).append("\t");
        buildAnswers(b);
    }
}
