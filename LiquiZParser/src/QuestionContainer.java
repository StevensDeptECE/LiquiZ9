import java.util.*;

public class QuestionContainer {
    private String qcId; // TODO: unique id for the question container, generated into html. Is it ever used?
    private String name;
    private float points;
    private ArrayList<DisplayElement> displayElements;

    public QuestionContainer() { displayElements = new ArrayList<>(); name = ""; }
    public QuestionContainer(float points, String name) {
        displayElements = new ArrayList<>();
        this.name = name;
        this.points = points;
    }
    private final void writeQuestionOrAnswer(StringBuilder b, int questionNum, boolean isQuestion) {
        String className = isQuestion ? "question" : "answer";
        b.append("<div class='")
            .append(className)
            .append("'>\n");

        if (isQuestion) {
           b.append("<div>")
            .append(questionNum)
            .append(". ")
            .append(name)
            .append("<span class='pts'>  (")
            .append(points)
            .append(" points)</span>" +
                    "<input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>" +
                    "</div>\n")
            .append('\n');
        }

        for (DisplayElement d : displayElements) {
            d.writeHTML(b, isQuestion);
        }
        b.append("</div>\n\n");
    }
    public void writeHTML(StringBuilder b, int questionNum) {
        writeQuestionOrAnswer(b, questionNum, true);
        writeQuestionOrAnswer(b, questionNum, false);
    }
    public void addPoints(float points){
        this.points+=points;
    }
    public void add(DisplayElement e) {
        displayElements.add(e);
    }
}
