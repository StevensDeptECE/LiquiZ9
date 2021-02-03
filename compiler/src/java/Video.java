import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class Video extends QuestionType {
  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    text.erase(0, 4);
    string temp = "media/" + text;
    buildString(replace, "<video controls width='320' height='240'><source src='",
                temp, "' type='video/mp4'></video>");
    return replace;
  }
}