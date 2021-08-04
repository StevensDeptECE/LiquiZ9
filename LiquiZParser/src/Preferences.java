import java.util.*;
import java.awt.*;
import java.time.Duration;

public class Preferences {
    //private Color textColor;
    //private Color backgroundColor;
    //private Color headerColor;
    private String honorCode;
    private String imgName;
    private Duration timeLimit;
    private double pointsPerQuestion;
    private HashMap<String, String> definitions;
    private HashMap<String, Color> colorMap;

    public Preferences(){
        definitions = createDefinitions();
        colorMap = createColorMap();
        pointsPerQuestion = 1.0;
        timeLimit = Duration.ofHours((long)1.0);
    }

    //private getPreferencesJSON() { }TODO: get the users preferences JSON file
    private HashMap<String, Color> createColorMap() {
        HashMap<String, Color> tempColorMap = new HashMap<>();
        tempColorMap.put("text", Color.yellow);
        tempColorMap.put("background", Color.red);
        tempColorMap.put("header", Color.blue);
        return tempColorMap;
    }
    private HashMap<String, String> createDefinitions() {
        HashMap<String, String> tempDefinitionsMap = new HashMap<>();
        tempDefinitionsMap.put("yesno", "yes,no");
        tempDefinitionsMap.put("truefalse", "true,false");
        tempDefinitionsMap.put("likert5", "Strongly agree,Agree,Neutral,Disagree,Strongly disagree");
        return tempDefinitionsMap;
    }
    public final String getDefinition(String answerName) {
        String preDefChoices = definitions.get(answerName);
        if(preDefChoices == null){
            throw new IllegalArgumentException();
        }
        return preDefChoices;//TODO error check
    }
    public final Color getColor(String colorName) {
        Color preDefColor = colorMap.get(colorName);
        if(preDefColor == null){
            throw new IllegalArgumentException();
        }
        return preDefColor;
    }

}