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
**/

public abstract class QuestionType { 
  protected LiquizCompiler compiler;
  protected String qID;
  protected String replace;
  protected String text;
  protected String defName;
  protected int fillSize;
  public static final Pattern dSet;
  public static final Pattern parseRandomVar;
  public static final Pattern parseVar;
  public static final Pattern parseMatrixQuestion;
  public HashMap<String, String> vars;


  static {
    dSet = Pattern.compile("(:.*:)");
    parseRandomVar = Pattern.compile("rnd:([a-zA-Z][a-zA-Z0-9_]*)\\{(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*))\\}");
    parseVar = Pattern.compile("var:([a-zA-Z][a-zA-Z0-9_]*)");
    parseMatrixQuestion = Pattern.compile("mat\\{(\\d+),(\\d+)\\}([^\\$]+)");
  }

  //contstructor for QuestionType
  public QuestionType() {
    vars = new HashMap<>();
  }

  public final String to_string(String s) {
    return s;
  }

  public String to_string(char c) {
    char s[2] = {c, '\0'};
    return to_string(s);
  }

  //TODO: fix these functions, might require templates (build and append)
  //template <typename... Args>
  public void buildString(string dest, final Args ... args) {
    dest.clear();
    int[] unpack = {0, (dest += toString(args), 0)...};
    (void)(unpack);
  }

  public void appendString(string dest, final Args ... args) {
    int[] unpack = {0, (dest += toString(args), 0)...};
    (void)(unpack);
  }

  public void setText(final String t) {
    text = t;
  }

  public String print(final LiquizCompiler compiler, ostream a, int pN, inr qN, double p) {

  }

  public addAnswer(String typeID, String qID, final string ans, double points, ostream answersFile, int partNum, int questionNum) {
    partNum++;
    buildString(qID, typeID, "_", questionNum, "_", partNum);
    answersFile = qID + "\t" + points + "\t" + ans + '\n';
  }

  public static double parse(final string v, double defaultVal) {
    try {
      return Double.parseDouble(v);
    } 
    catch (final exception e) {
      System.out.print("Error parsing string " + v + " as double.\n");
      return defaultVal;
    }
  }

  public static long parse(final string v, long defaultVal) {
    try {
      return Double.parseDouble(v);
    } 
    catch (final exception e) {
      System.out.print("Error parsing string " + v + " as double.\n");
      return defaultVal;
    }
  }
}