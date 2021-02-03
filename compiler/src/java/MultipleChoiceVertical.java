import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class MultipleChoiceVertical extends QuestionType {
  private String temp, input, answer;
  private String typeID = 'q';
  private String option = "";

  public void getAnswer() {
    input = "";
    for (int i = 0; i < answer.length(); i++) {
      if (answer[i] == '*') {
        for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
          input += answer[j];
        }
        answer.erase(i, 1);
      }
    }
  }

  public void getOptions() {
    int count = 1;
    buildString(temp, "<input type='radio' name='", qID, "' value='");
    replace = "        <div class='horizontal'>";
    for (int i = 0; i <= answer.length(); i++) {
      if (answer[i] == ',' || i == answer.length()) {
        appendString(replace, "<label>", temp, option,  "'>", option, "</label>");
        option = "";
        count++;
      } 
      else {
        option += answer[i];
      }
    }
    count = 1;
    replace += "\n</div>";
  }

  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    input = "";
    answer = text;
    smatch m;
  
    if (regex_search(answer, m, dSet)) {
      answer = text.erase(0, 4);
      defName = "";
      for (int i = 0; answer[i] != ':'; i++) {
        defName += answer[i];
      }
      answer.erase(0, defName.length() + 1);
    } 
    else {
      answer = text.erase(0, 4);
    }
  
    getAnswer();
    addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
    getOptions();
    return replace;
  }
}
