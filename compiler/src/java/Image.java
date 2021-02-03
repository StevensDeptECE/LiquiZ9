import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class Image extends QuestionType {
  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    text.erase(0, 4);
    String temp = "media/" + text;
    buildString(replace, "<img src='", temp, "'></img>");
    return replace;
  }
}