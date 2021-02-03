import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class FillIn extends QuestionType {
  private static String fillinStyle = "fillin";
  private static long defaultLen;
  private static final HashMap<Char, String> fillTypes = new HashMap<Char, String>();
  private String answer, typeID, size, orig;
  private int len;
    
  public void getFillinType(final char type) {
    if(fillTypes.find(type) != fillTypes.end()) {
      typeID = type;
    }
    else {
      typeID = "q";
    }
  }

  //string FillIn::fillinStyle = "fillin"; // default fillin style

  public final String getStyle() {
    return fillinStyle;
  }
    
  public final long getDefaultLen() {
    return defaultLen;
  }
    
  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    replace = "";
    getFillInType(text[1]);

    if (typeID != "q") {
      answer = text.erase(0, 2);
    } 
    else {
      answer = text.erase(0, 1);
    }

    if (answer.charAt(0) == '{') {
      for (int i = 1; answer.charAt(i) != '}'; i++) {
        size += answer.charAt(i);
      }

      len = stoi(size);
      answer.erase(0, size.length() + 3);
    } 
    else {
      answer.erase(0, 1);
    }

    addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
    buildString(replace, "<input class='", getStyle(), "' name='", qID, "' type='text' id='", qID,
                "' size='", len, "'/>");
    size = "";
    answer = "";
    len = 6;
    typeID = "";
    return replace;
  }

  public void FillHashMap() {
    fillTypes.put('Q', "case insensitive");
    fillTypes.put('s', "space insensitive");
    fillTypes.put('n', "numeric");
    fillTypes.put('S', "space and case insensitive");
  }
}