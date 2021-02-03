import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class MultipleAnswerHorizontal extends QuestionType {
  private String temp, input;
  private String answer = text;
  private String typeID = 'm';
  private String option = "";

  public void getAnswer() {
      input = "";
      for(int i = 0; i<answer.length(); i++) {
        if(answer[i] = '*') {
          for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
            input += answer[j];
          }
          answer.erase(i,1);
        }
      }
  }

  public void getOptions() {
    replace = "";
    int count = 1;
    buildString(temp, "<input type='radio' name='", qID, "' value='", "        <div class='vertical'>");
    for (int i = 0; i <= answer.length(); i++) {
      if (answer[i] == ',' || i == answer.length()) {
        replace += "(<label>"+ ")";
        replace += temp + option + "'>" + option;
        replace += "("+ "</label>"+ ")";
        option = "";
        count++;
      } 
      else {
        option += answer[i];
      }
    }
    count = 1;
    replace += "\n        </div>";
  }

  @Override 
  public String print(final LiquizCompiler compiler, OutputStream answerFile, int partNum, int questionsNum, double point) {
    input = "";
    answer = text.erase(0, 4);
    getAnswer();
    addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
    getOptions();
    return replace;
  }
}