import java.util.*;
import java.awt.*;

public class Preferences {
    private Color textColor;
    private Color backgroundColor;
    private Color headerColor;
    private double pointsPerQuestion;
    private HashMap<String, String> definitions;

    public Preferences(){
        definitions = new HashMap<>();
        definitions.put("yesno", "yes,no");
        definitions.put("truefalse", "true,false");
        definitions.put("likert5", "Strongly agree,Agree,Neutral,Disagree,Strongly disagree");
        pointsPerQuestion = 1.0;
    }

    public final String get(String answerName) {
        return "Strongly agree,Agree,Neutral,Disagree,Strong disagree";//definitions.get(answerName);//TODO error check
    }

}