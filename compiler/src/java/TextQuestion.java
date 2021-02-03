import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class TextQuestion extends QuestionType {
    private String answer = "N/A";
    private String typeID = "T";

    @Override 
    public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
      addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
      text.erase(0, 4);
      buildString(replace, "<textarea rows='10' cols='60' name='",
                  qID, "' id='", qID, "'>", text, "</textarea>");
      return replace;
    }
  }