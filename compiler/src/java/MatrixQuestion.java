import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class MatrixQuestion extends QuestionType{
    private long rows, cols; // the size of the matrix
    private long inputLen;
    private String matrixList; // the list of comma-separated values to be displayed
                                // an underscore (_) means a numeric fillin question
                                // an asterisk (*) means a text fillin question

    @Override
    public void setText(final String body) {
      // TODO: regex fix for java
      Matcher m;
      regex_search(body, m, parseMatrixQuestion); // parse body of matrix question
      rows = parse(m[1], 1U);
      cols = parse(m[2], 1U); // get matrix size, default to 1x1
      matrixList = m[3];  
      inputLen = 6;
    }

    @Override
    public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
      tring token, answer, typeID;
      //token.reserve(64);
      //answer.reserve(64);
      //typeID.reserve(2);
      istringstream matS = (matrixList); //convert string stream to Java equivalent
      replace = "<table class='mat'>\n";
      for (long r = 0; r < rows; r++) {
        replace += "<tr>";
        for (long c = 0; c < cols; c++) {
          replace += "<td>";
          if (!getline(matS, token, ',')) {
            int lineNum = 0; // TODO: add compiler pointer to setText parameters
            System.err.println("Error parsing MatrixQuestion line:" + lineNum + '\n');
            replace += "\n</tr>\n</table>\n";
            return replace;
          }
          if (token[0] == underscore) {
            if (token.length() >= 3 && token[2] == ':') {
              switch(token[1]) {
                case 'Q':
                case 's':
                case 'n':
                  typeID = String(1, token[1]);
              }
            } else {
              typeID = "n"; // default type for matrix question is numeric
            }
            answer = token.substr(1);
            addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
            appendString(replace, "<input class='mat' name='", qID,
                        "' type='text' id='", qID, "' size='", inputLen, "'/>");
          } else {
            replace += token;
          }
          replace += "</td>";
        }
        replace += "</tr>\n";
      }
      replace += "</table>\n";
      return replace;
    }
}