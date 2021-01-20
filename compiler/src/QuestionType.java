import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

/**
  @author: Joseph Insalaco
  @author: Brian Ng
  @author: Kayleen Chin 
  @author: William Zheng
  @author: Bernard Tran
  @author: Dov Kruger
*/

public class QuestionType extends LiquizCompiler
{     
    protected LiquizCompiler compiler;
    protected string qID;
    protected string replace;
    protected string text;
    protected string defName;
    protected int fillSize;

    public static Pattern dSet;
    public static Pattern parseRandomVar;
    public static final Pattern parseVar;
    public static Pattern parseMatrixQuestion;

    public HashMap<String, String> vars = new HashMap<String, String>();
    public default_random_engine generator;

    // constexpr char underscore = '_';

    static {
      dSet = Pattern.compile("(:.*:)");
      parseRandomVar = Pattern.compile("rnd:([a-zA-Z][a-zA-Z0-9_]*)\\{(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*))\\}");
      parseVar = Pattern.compile("var:([a-zA-Z][a-zA-Z0-9_]*)");
      parseMatrixQuestion = Pattern.compile("mat\\{(\\d+),(\\d+)\\}([^\\$]+)");
    }

    public final string to_string(final string s) {
      return s;
    }

    public string to_string(char c) {
      char s[2] = {c, '\0'};
      return string(s);
    }

    //TODO: fix these functions, might require templates (build and append)
    //template <typename... Args>
    public void buildString(string dest, final Args ... args) {
      dest.clear();
      int[] unpack = {0, (dest += to_string(args), 0)...};
      static_cast<void>(unpack);
    }

    public void appendString(string dest, final Args ... args) {
      int[] unpack = {0, (dest += to_string(args), 0)...};
      static_cast<void>(unpack);
    }

    public void setText(final string t) {
      text = t;
    }

    public string print(final LiquizCompiler compiler, ostream a, int pN, inr qN, double p) {

    }

    public addAnswer(string typeID, string qID, final string ans, double points, ostream answersFile, int partNum, int questionNum) {
      partNum++;
      buildString(qID, typeID, "_", questionNum, "_", partNum);
      answersFile = qID + "\t" + points + "\t" + ans + '\n';
    }


    public class MultipleChoiceHorizontal {
      private string temp, input, answer;
      private string typeID = 'q';
      private string option = "";

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
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
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

    public class MultipleChoiceVertical {
      private string temp, input, text;
      private string typeID = 'q';
      private string option = "";

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
        replace = "";
        int count = 1;
        buildString(temp, "<input type='radio' name='", qID, "' value='", "        <div class='vertical'>");
        for (int i = 0; i <= answer.length(); i++) {
          if (answer[i] == ',' || i == answer.length()) {
              replace += "(<label>)";
              replace += temp + option + "'>" + option;
              replace += "(</label>)";
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
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        input = "";
        answer = text.erase(0, 4);
        getAnswer();
        addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
        getOptions();
        return replace;
      }
    }
    
    public class MultipleAnswerHorizontal {
      private string temp, input, answer;
      private string typeID = 'm';
      private string option = "";

      public void getAnswer() {
        input = "";
        for (int i = 0; i < answer.length(); i++) {
          if (answer[i] == '*'){
            for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
              input += answer[j];
            }
            answer.erase(i, 1);
          }
        }
      }

      public void getOptions() {
        replace = "";
        int count = 1;
        buildString(temp, "<input type='checkbox' name='", qID, "' value='");
        replace += "(<div class='horizontal'>)";
        for (int i = 0; i <= answer.length(); i++) {
          if (answer[i] == ',' || i == answer.length()) {
            replace += "(<label>)";
            replace += temp + option + "'>" + option;
            replace += "(</label>)";
            option = "";
            count++;
          } 
          else {
            option += answer[i];
          }
        }
        count = 1;
        replace += "(</div>)";
      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        input = "";
        answer = text.erase(0, 4);
        getAnswer();
        addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
        getOptions();
        return replace;
      }
    }
    
    public class MultipleAnswerVertical {
      private string temp, input;
      private string answer = text;
      private string typeID = 'm';
      private string option = "";

      public void getAnswer() {
          input = "";
          for(int i = 0; i<answer.length(); i++) {
            if(answer[i] = '*'){
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
      public string print(final LiquizCompiler compiler, OutputStream answerFile, int partNum, int questionsNum, double point) {
        input = "";
        answer = text.erase(0, 4);
        getAnswer();
        addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
        getOptions();
        return replace;
      }
    }

    public class FillIn {
      private static string fillinStyle = "fillin";
      private static long defaultLen;
      private static final HashMap<Char, String> fillTypes = new HashMap<Char, String>();
      private string answer, typeID, size, orig;
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

      public final string getStyle() {
        return fillinStyle;
      }
        
      public final long getDefaultLen() {
        return defaultLen;
      }
        
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        replace = "";
        getFillInType(text[1]);

        if (typeID != "q") {
          answer = text.erase(0, 2);
        } 
        else {
          answer = text.erase(0, 1);
        }

        if (answer[0] == '{') {
          for (int i = 1; answer[i] != '}'; i++) {
            size += answer[i];
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

      public class Hex {
        private static string hexStyle = "hex"; // Style for hex questions
        private static long defaultLen = 8;

        public final string getStyle() {
          return hexStyle;
        }

        public final long getDefaultLen() {
          return defaultLen;
        }
      }

      public class OpCode {
        private static string opcodeStyle = "opcode"; // Style for opcode questions
        private static long defaultLen = 5;

        public final string getStyle() {
          return opcodeStyle;
        }

        public final long getDefaultLen() {
          return defaultLen;
        }
      }

      public class Command {
        private static string commandStyle = "command"; // Style for command line questions, longer by default
        private static long defaultLen = 20;

        public final string getStyle(){
          return commandStyle;
        }

        public final long getDefaultLen(){
          return defaultLen;
        }
      }
    }

    public class TextQuestion {
      private string answer = "N/A";
      private string typeID = "T";

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
        text.erase(0, 4);
        buildString(replace, "<textarea rows='10' cols='60' name='",
                    qID, "' id='", qID, "'>", text, "</textarea>");
        return replace;
      }
    }

    public class DropDownQuestion {
      private string answer, option, input;
      private string typeID = "q";
      private int count = 0;
      
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
        replace = "(<select class='' name=')";
        replace += qID + "'" + "id='" + qID + "'>";
        for (int i = 0; i <= answer.length(); i++) {
          if (answer[i] == ',' || i == answer.length()) {
            replace += "("+
              "<option value=')" + option + "'>" + option + "</option>";
            option = "";
          } else {
            option += answer[i];
          }
        }
        replace += "("+
          "</select>)";
      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        input = "";
        answer = text.erase(0, 4);
        getAnswer();
        addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
        getOptions();
        answer = "";
        return replace;
      }
    }

    public class Image {
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        text.erase(0, 4);
        string temp = "media/" + text;
        buildString(replace, "<img src='", temp, "'></img>");
        return replace;
      }
    }

    public class Video {
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        text.erase(0, 4);
        string temp = "media/" + text;
        buildString(replace, "<video controls width='320' height='240'><source src='",
                    temp, "' type='video/mp4'></video>");
        return replace;
      }
    }

    public class Definition {
      private string defs, answer, name, option;
      private string typeID = "q";
      private int count = 0;

      public void getOption() {
        //TODO: support any form of multiple choice with predefined definitions, not just dropdowns
  
        // build a string with the select and the first option which is empty. By default, question is unanswered unless
        // the user selects something.
        buildString(replace, "<select class='dro' name='",  qID, "' id='", qID, "'><option value=''></option");
        for (int i = 0; i <= defs.length(); i++) {
          if (defs[i] == ',' || i == defs.length()) {
            replace += "("+
                  "<option value=')" + option + "'>" + option + "</option>";
            option = "";
          } 
          else {
            option += defs[i];
          }
        }
        replace += "("+"</select>)";
      }
      
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        answer = text.erase(0, 4);

        for (int i = 0; answer[i] != ':'; i++) {
          name += answer[i];
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
  
    public static double parse(final string v, double defaultVal) {
      try {
        return stod(v);
      } 
      catch (final exception e) {
        System.out.print("Error parsing string " + v + " as double.\n");
        return defaultVal;
      }
    }
    public static long parse(final string v, long defaultVal) {
      try {
        return stod(v);
      } 
      catch (final exception e) {
        System.out.print("Error parsing string " + v + " as double.\n");
        return defaultVal;
      }
    }
    
    public class RandomVar {

      private string var;
      private double min, max, inc;
      private string typeID = "r";

      @Override
      public void setText(final string body) {
        System.out.print("Random var " + body + "\n");
        smatch m;
        regex_search(body, m, parseRandomVar);
        System.out.print("matches:" + m[1] + "," + m[2] + "," + m[3] + "," + m[4] + '\n');
        string varName = m[1];
        System.out.print("varname" + varName + '\n');
        min = parse(m[2], 0.0); // parse and give double precision defaults if not found
        inc = parse(m[3], 1.0);
        max = parse(m[4], 10.0);
        System.out.print("var =" + varName + " min=" + min + " inc=" + inc + " max=" + max + '\n');
        Long numRandom = ceil((max-min)/inc);
        Random randomvar = new Random();
        long r = randomvar.nextLong();
        double rval = min + r * inc;
        vars[varName] = to_string(rval);
      }

      public void getVar() {
        for (int i = 1; text[i] != '}'; i++) {
          var += text[i];
        }
      }

      public void getRange() {
        /*
        #if 0
        for (int i = 1; text[i] != ','; i++) {
          minVal += text[i];
        }
        min = stod(minVal);
        text.erase(0, minVal.length() + 2);
        
        for (int i = 1; text[i] != ','; i++) {
          increm += text[i];
        }
        inc = stod(increm);
        text.erase(0, increm.length() + 1);
        
        for (int i = 1; text[i] != ')'; i++) {
          maxVal += text[i];
        }
        max = stod(maxVal);
        text.erase(0, maxVal.length() + 1);
        #endif
        */
      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        text.erase(0, 3);
        getVar();
        text.erase(0, var.length() + 3);
        if (text[0] == '(') {
          getRange();
        }
      
        cout << *this << '\n';
      
        return "";
      }

      //TODO: look up overloading operators
      OutStream operator(ostream s, final RandomVar r) {
        return s + r.min + "," + r.inc + "," + r.max;
      }
    }

    public class Variable {
      private string name;

      @Override
      public void setText(final string body) {
        Matcher m;
        regex_search(body, m, parseVar);
        name = m[1];
        System.out.print("var " + name + "\n");
      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        if (vars.find(name) != vars.end()) {
          System.out.print("variable " + name + "==>" +  vars.at(name) + '\n'); //TODO: compiler->vars?
      
          return vars.at(name);
        }
        System.out.print("Undefined variable " + name + '\n');
        return "";
      }
    }

    /*
    A formula question evaluates a set of variables and can then ask the student to compute any variable
    */
    public class FormulaQuestion {
      @Override
      public void setText(final string body) {

      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        System.out.print("formula\n");
        return "formula";
      }
    }

    public class MatrixQuestion {
      private long rows, cols; // the size of the matrix
      private long inputLen;
      private string matrixList; // the list of comma-separated values to be displayed
                                 // an underscore (_) means a numeric fillin question
                                 // an asterisk (*) means a text fillin question

      @Override
      public void setText(final string body) {
        // TODO: regex fix for java
        Matcher m;
        regex_search(body, m, parseMatrixQuestion); // parse body of matrix question
        rows = parse(m[1], 1U), cols = parse(m[2], 1U); // get matrix size, default to 1x1
        matrixList = m[3];  
        inputLen = 6;
      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
        string token, answer, typeID;
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
                    typeID = string(1, token[1]);
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
}