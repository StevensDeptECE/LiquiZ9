public abstract class DisplayElement {
    public double getPoints(){ return 0.0; }
    public void writeAnswers(StringBuilder b) { return; };
    public abstract void writeHTML(StringBuilder b, boolean isQuestion);

}
