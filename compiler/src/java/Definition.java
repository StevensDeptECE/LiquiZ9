import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class Definition extends QuestionType {
  private String defs, answer, name, option;
  private String typeID = "q";
  private int count = 0;

  public void getOption() {
    //TODO: support any form of multiple choice with predefined definitions, not just dropdowns

    // build a string with the select and the first option which is empty. By default, question is unanswered unless
    // the user selects something.
    buildString(replace, "<select class='dro' name='",  qID, "' id='", qID, "'><option value=''></option");
    for (int i = 0; i <= defs.length(); i++) {
      if (defs.charAt(i) == ',' || i == defs.length()) {
        replace += "("+
              "<option value=')" + option + "'>" + option + "</option>";
        option = "";
      } 
      else {
        option += defs.charAt(i);
      }
    }
    replace += "("+"</select>)";
  }
  
  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    answer = text.erase(0, 4);

    for (int i = 0; answer.charAt(i) != ':'; i++) {
      name += answer.charAt(i);
      count++;
    }

    compiler = findDefinitions(name, defs);
    answer = answer.erase(0, count + 1);

    addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
    getOptions();
    answer = "";
    name = "";
    count = 0;
    return replace;
  } 
}