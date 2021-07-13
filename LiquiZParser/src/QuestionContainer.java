import java.util.*;

public class QuestionContainer {
    private ArrayList<DisplayElement> displayElements;

    public QuestionContainer() { displayElements = new ArrayList<>(); }
    public void writeHTML(StringBuilder b) {
        for (DisplayElement d : displayElements) {
            d.writeHTML(b);
        }
    }
    public void add(DisplayElement e) {
        displayElements.add(e);
    }
}
