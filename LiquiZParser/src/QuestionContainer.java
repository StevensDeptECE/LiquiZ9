import java.util.*;

public class QuestionContainer {
    private ArrayList<DisplayElement> displayElements;

    public QuestionContainer() { displayElements = new ArrayList<>(); }
    public void writeHTML(StringBuilder b) {
        b.append("<div class='question'>\n");
        for (DisplayElement d : displayElements) {
            d.writeHTML(b);
        }
        b.append("</div>\n");
    }
    public void add(DisplayElement e) {
        displayElements.add(e);
    }
}
