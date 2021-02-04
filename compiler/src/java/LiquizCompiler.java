import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.io.InputStream;
import java.io.RandomAccessFile;
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

public class LiquizCompiler {
  static String emptystr;
  static String escapedDollar;
  static String defaultQuiz;
  static final long defaultFillInBlankSize; // (long is used as an unsigned integer)
  private static final Random r;
  private static final Pattern questionStart;
  private static final Pattern specials;
  private static final Pattern qID;
  private static final Pattern questionPattern;
  private static long uuid; // (long is used as an unsigned integer)
  private static final HashMap<String, QuestionType> questionTypes;
  private HashMap<String, String> variables;
  private HashMap<String, String> definitions;
  //class Definition {}
  private String DELIM;
  private String questionText;
  private String inputText;
  private String answerText;
  private String answerInput;
  private String outputDir; // directory where the public output goes (html)
  
  //TODO: need to generate randomized numbers for the images so that file names do not 
  //give away answers and place in the output directory
  private PrintWriter html;
  private OutPutStream answers;
  private OutputStream xml;       //TODO: to export to canvas, generate a qti zip file containing XML
  private InputStream liquizFile;
  private long cursor;            // byte offset into quiz file (long is used as an unsigned integer)
  private char bytes;             // underlying bytes in quiz
  private long fileSize;          // size of quiz text in bytes (long is used as an unsigned integer)
  private QuestionType defaultQuestionType; //TODO write questiontype object
  private int logLevel;           // verbose level, how much debugging to display
  private int questionNum;        // the number of the current question
  private int partNum;            // the subnumber within each question
  private int lineNumber;         // line number within the .lq file
  //private int questionLineNumber; 
  private double questionCount;   // number of question inputs in the current question
  private double points;          // total number of points in the quiz
  private int fillSize;           // default number of characters in a fill-in question
  private int timeLimit;          // number of minutes to take the quiz, 0 means untimed
  private String imgFile, styleSheet, quizName, license, copyright, author, email;
        
  static {
    // ideally everything you do to initialize the class should be in here
    r = new Random(0); // TODO: once you are done testing, remove the zero, make this initialize from time
    questionTypes = new HashMap<>();
    questionStart = Pattern.compile("^\\{");
    specials = Pattern.compile("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");
    qID = Pattern.compile("name='[q||T||Q||m||s||n||S]_[0-9]*_[0-9]*'");
    questionPattern = Pattern.compile("\\$(?:mch|mcv|dro|mah|mav|fQ|fq|fn|fs|fS)\\$");

    uuid = 1;

    emptystr = "";
    escapedDollar = "\\$";
    defaultQuiz = "quiz.css";
    defaultFillInBlankSize = 6;
  }

  //this is the constructor for Liquiz compiler class
  public LiquizCompiler(String filename) throws IOException {
    // style: everything you do to initialize object should be in here
    variables = new HashMap<>();
    definitions = new HashMap<>();
    buildQuestionHashMap();
    DELIM = "---";
  }
  
  private void setLogLevel(int level) {
    logLevel = level; 
  }
    
  private void findQuestionType(String type, double points, String delim, int pos, int len) {
    QuestionType question = (questionTypes.containsKey(type) != questionTypes.lastEntry());
    if (question != nullptr) {
      question = setText(delim);
      inputText = question.print(this, answers, partNum, questionNum, points);
      questionText = questionText.replace(pos, len, inputText); //TODO: fix this
    }
  }

  private String removeExtension(final char fileName[]) {
    int i;
    for (i = 0; fileName[i] != '\0'; i++);
    for (; i >= 0 && fileName[i] != '.'; i--);
    return filename.substring(fileName, i + 1);
  }

  private void findDefinitions(String name, String defs) {
    if (!definitions.containsKey(name)) {
      System.out.println("missing definition " + name + " on line " + questionLineNumber);
    } 
    else {
      defs = definitions.get(name);
    }
  }

  private void includeQSpec(JSONObject parentQuizSpec, String filename) throws IOException {
    FileInputStream spec = new FileInputStream("spec/" + filename);
    BufferedReader specFile = new BufferedReader(new InputStreamReader(spec));
    //Half of these functions look like they only exist because of C++ see what we can remove
    //char dirName[];
    //System.err.println( "Current directory: " + getcwd(dirName, sizeof(dirName)) + '\n');
    //System.err.println( "filename: " + "spec/" + filename);
    //if (!specFile.good()) {
    //  System.err.println( "Cannot open file " + filename);
    //}
    JSONObject specInfo = new JSONOBject(specFile);
    //Not sure what we're parsing in here, seems to be handled by above
    //specInfo = parse(specFile);

    if (logLevel >= 3) {
      System.err.println( "dumping qspec json before merge");
      for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
        System.err.println( i.key() + "==>" + i.value());
    }
      //TODO: Looks like this function doesn't work atm in C++ either, find a way to merge the qspec files
    if (specInfo.get("parent") != specInfo.end()) {
      includeQSpec(parentQuizSpec, specInfo.at("parent")); //TODO: merge specInfo on top of parentQuizSpec
      //TODO: merge isn't working, so comment out?
      merge(parentQuizSpec, specInfo);
      specInfo = parentQuizSpec;
    }
    if (parentQuizSpec != Null) {// only the first level sets all the variables below
      parentQuizSpec = specInfo;
      return;
    }
    
    // specInfo should now contain the merged specification of all recursive files
    if (logLevel >= 3) {
      for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
        System.err.println( i.key() + "==>" + i.value());
    }

    lineNum = 1;
  // TODO: check error on all these. If defaults does not exist, do nothing?
    if (specInfo.find("defaults") != specInfo.end()) {
      //TODO: How to find line number within the JSON?
      JSONObject defaults = specInfo.at("defaults");
      imgFile = lookup(defaults, "img", emptystr, 1);
      styleSheet = lookup(defaults, "stylesheet", defaultQuiz, lineNum);
      fillSize = lookup(defaults, "fillInTheBlankSize", defaultFillInBlankSize, lineNum);
      timeLimit = lookup(defaults, "timeLimit", 0, lineNum); // default is untimed
    }
    email = lookup(specInfo, "email", emptystr, lineNum);
    author = lookup(specInfo, "author", emptystr, lineNum);

    if (specInfo.find("def") != specInfo.end()) {
      for (JSONArray it = specInfo.at("def").begin(); it != specInfo.at("def").end(); ++it) {
        String name = it.key();
        String defs;
        for (int i = 0; i < it.value().size(); i++) {
          String defVal = it.value()[i];
          defs += defVal;
          defs += ",";
        }
        defs.erase(defs.size()-1, 1);
        definitions[name] = defs;
        if (logLevel >= 3) {
          System.err.println( "DEF " + name + "==>" + defs);
        }
        answers += "defs" + "\t" + name + "\t" + defs + "\n";
      }
    }
  }

  private void getJSONHeader() {
    String line;
    String specName;
    if (!getline(line)) {
      System.err.println("Unexpected end of file line while getting JSON header");
      return;
    }
    JSONObject header = parse(line);
    /*#if 0
      if (expect(specName, this, header, "quizspec")) {
        includeQSpec(specName);
      }
    #endif
      if (header.find("quizspec") != header.end()) {
      specName = header.at("quizspec");//TODO: pull error checking into separate function above
      includeQSpec(nullptr, specName);
      }  */
    quizName = header.at("name"); //TODO: error checking
  }

  private void setAnswer() {
      Matcher m;
      answerText = questionText;
      int pos, end;
      while (find(answerText, m, qID)) {
        pos = m.position();
          for (int i = m.position(); answerText.charAt(i) != '='; i++) {
            pos++;
          }
        answerText.insert(pos+2, "a");
      }
  }

  private void generateHeader() {
    getJSONHeader();
    StringBuilder b = new StringBuilder(32768);
    b.append("<!DOCTYPE html>\n")
      .append("<html>\n")
      .append("<head>\n")
      .append("  <meta charset='UTF-8'/>\n")
...
    .append("  <link rel='stylesheet' type='text/css' href='css/".append(youtrvariable)
    .append("')\n"
    html.println(b); // will call b.toString() which is ugly but oh well

    html.printf( 
    "<!DOCTYPE html>\n"+
    "<html>\n"+
    "<head>\n"+
    "  <meta charset='UTF-8'/>\n"+
    "  <link rel='stylesheet' type='text/css' href='css/%s')\n" +
        styleSheet +
    "('>"+
    "  <title>"+
    "    LiQuiz [)" +
        quizName +
    "(]"+
    "  </title>"+
    "  <script src='js/quiz.js'></script>"+
    "</head>"+
    "<body onload='startTime()" +
    timeLimit + ")'>" +
    "("+
    "  <form method='get' action='/Grade'></form>"+
    "  <!-- Header -->"+
    "  <div id='header' class='header'>"+
    "    <img class='logo' src='media/)" +
      imgFile + "'/>" +
    "("+
    "  <div class='headerText'>"+
    "    <div class='quizTitle'>"+
          ")" +
      quizName +
    "("+
    "  </div>"+
    "  <div class='headerDetails'>"+
    "    <div class='headerRow'>"+
    "      )" +
      author +
    "("+
    "    </div>"+
    "    <div class='headerRow'>"+
    "      Email  )" +
            email + " if you have any questions!" +
    "("+
    "    </div>"+
    "      <div class='headerRow'>"+
    "        <input id='pledge' type='checkbox' name='pledged' value='pledged'/>"+
    "        <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>"+
    "      </div>"+
    "      <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>"+
    "      <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>"+
    "    </div>"+
    "    </div>"+
    "    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>"+
    "  </div>"+
    ")");
  }
  
  private void makeQuestion(JSONObject question) {
    String style;
    require(question, "style", style, lineNum);
    String preStart, preEnd;
    if (style == "pcode" || style == "code") {
      preStart = "<pre class='" + style + "'>";
      preEnd = "</pre>";
    } else {
        preStart = "<pre class='text'>";
        preEnd = "</pre>";
      }
    if (style != "def") {
    //    String temp = question.at("points");
    double totalPoints = lookup(question, "points", 0, lineNum);
    String questionName = lookup(question, "name", emptystr, lineNum);
    Matcher m;
    String end = "</p>";
    partNum = 0;
    while (find(questionText, m, specials)) {
      String delim = m[2];

      int pos = questionText.find("<p hidden>", m.position());
      int endPos = questionText.find(end, m.position());
      long questionLineNum = stoi(questionText.substr(pos+10, endPos-pos-10+1)); //TODO: get rid of this!

      String type;
      if (delim[0] != 'f') {
        for (int i = 0; delim[i] != ':' && delim[i] != '{'; i++) {
          type += delim[i];
        }
      } else {
        type = delim[0];
      }
      findQuestionType(type, points, delim, m.position(), m.length());
    }

    double points = totalPoints != 0 ? totalPoints / questionCount : partNum;
      questionText.pop_back();  
      setAnswer();
      
    System.out.printf(html + "("+
    "<div class='section'>"+
    "  <div class='question' id='q)"+
      html + questionNum + "'>"+
      html + "("+
    "    <div>"+
    "      )"+
      html + questionNum + "." + "\t" + questionName+
      html + "("+
    "      <span class='pts'>  )"+
      html + "(" + totalPoints+ " points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>"+
      html + "("+
    "    </div>"+
    "    )"+
      html + preStart + '\n'+
      html + questionText + preEnd+

      html + "("+
    "  </div>"+
    "  <div class='answer'>"+
    "    )"+
      html + preStart+
      html + "("+
    ")"+
      html + answerText + preEnd+
      html + "("+
    "  </div>"+
    "</div>"+
    ")");
    
      questionNum++;
    } else {
      String defs = lookup(question,"values", emptystr, lineNum);
      String name = lookup(question,"name", emptystr, lineNum);
      definitions[name] = defs;
      answers = "defs"+"\t" + name + "\t" + defs +'\n';
    }
  }
  
  private void grabQuestions() {
    String line, qID, temp;
    Matcher m;
    lineNumber = 1;
    
    while ((line = liquizFile.readLine()) != null) {
      if (find(line, m, questionStart)) {  // looking for the beginning of a question
        JSONObject question;  // gets the question header
        s = question;
        lineNumber++;
        
        while (getline(liquizFile, line) != liquizFile.eof() && line != DELIM) {  // gets line within question section
          lineNumber++;
          questionText = questionText + line + "<p hidden>" + toString(lineNumber) + "</p>";
          questionText += '\n';
        }
        lineNumber++;
        for (int i = 0; i < questionText.length(); i++) {
          if (questionText[i] == '$') {
            questionCount++;
          }
        }

        questionCount /= 2;
        makeQuestion(question);
        questionCount = 0;
        questionText = "";
      }
    }
  }
  
  private void generateFooter() {
    System.out.printf(
      "<div class='footer'>" +
      "<pre id='bottomTime'></pre>" +
      "<input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>" +
      "</div>" + 
      "</form>" +
      "</body>" +
      "</html>");
  } 
  
  private void closeFile() {
    bytes = null;
    liquizFile.close();
    html.close();
    answers.close();
  }
  
  private void readFile(final char filename[], char bytes, long fileSize) { // (long is used as an unsigned integer)
    //ifstream f(fileName, ios::in | ios::ate);
    InputStream f = new FileInputStream(fileName);
    
    /*if (!f.good()) {
      throw fileName; //TODO: add Ex object
    }*/

    fileSize = f.available();
    //f.seekg(0, ios::beg);
    f.reset();
    bytes = new char[fileSize];
    f.read();
    f.close();
  }
  
  private bool getline(String line) {
    while (cursor < fileSize && bytes[cursor] == '#') { // skip comment
      cursor++;
      while (cursor < fileSize && bytes[cursor] != '\n')
        cursor++;
      lineNum++;
    }
    if (cursor >= fileSize)
      return false;
    long startLine = cursor;
    while (cursor < fileSize && bytes[cursor] != '\n')
      cursor++;
    line = new string(bytes[startLine], cursor - startLine + 1);
    lineNum++;
    cursor++;
    return true;
  }

  private void buildQuestionHashMap() { //hashmap for question types
    questionTypes.put("mch", new MultipleChoiceHorizontal());
    questionTypes.put("mcv", new MultipleChoiceVertical());
    questionTypes.put("mah", new MultipleAnswerHorizontal());
    questionTypes.put("mav", new MultipleAnswerVertical());
    questionTypes.put("f", new FillIn());
    questionTypes.put("tar", new TextQuestion());
    questionTypes.put("def", new Definition());
    questionTypes.put("dro", new DropDownQuestion());
    questionTypes.put("img", new Image());
    questionTypes.put("vid", new Video());
    questionTypes.put("rnd", new RandomVar());
    questionTypes.put("var", new Variable());
    questionTypes.put("for", new FormulaQuestion());
    questionTypes.put("mat", new MatrixQuestion());
  }

  //private StringBuilder quizCode; // the code for the current quiz
    
  public final int getLineNumber() {
    return lineNumber;
  }

  public void generateQuiz(final char liquizFileName[]) {
    String baseName = liquizFilename.substring(0, liquizFilename.length - 2);
    String htmlFilename = baseName + ".html";
    String ansFilename = baseName + ".ans";
    html = new PrintWriter(new BufferedWriter(new FileWriter(htmlFilename)));
    ans = new PrintWriter(new BufferedWriter(new FileWriter(ansFilename)));

    generateHeader();
    grabQuestions();
    generateFooter();
    closeFile();
  }
  
  public void dumpVariables() {
    for (auto def : variables) { 
      System.out.print(def.first + "\t==>\t" + def.second + '\n');
    }
  }

  // < T > is the template declaration, also knows as Generics in Java
  public static < T > T lookup(JSONObject json, String key, final T defaultVal, int lineNum) {
    try {
      auto it = json.find(key);
      if (it != json.end())
        return T(it.value());
      } catch (Exception e) {
        System.out.println( "Error parsing json value "+ key + " of type " + nameof(defaultVal) + e.what());
      }
    return defaultVal;
  }

  public static < T > void require(JSONObject json, String key, T target, int lineNum) {
    try {
      auto it = json.get(key);
      if (it != json.end())
        target = it.value();
      } catch (Exception e) {
        System.out.println("Expected " + key + " at line " + lineNum);
    }
  }

  public String nameof(String a) {
    return "string";
  }
  
  public String nameof(int a) {
    return "int";
  }

  public static JSONObject merge(final JSONObject a, final JSONObject b) {
    JSONObject result = a.flatten();
    JSONObject tmp = b.flatten();
    for ( auto it = tmp.begin(); it != tmp.end(); ++it )
      result[it.key()] = it.value();
    return result.unflatten();
  }

  public static final char name(double a) {
    return "double";
  }
  
  public static String name(int a) {
    return "int";
  }
  public static void main(String[] args) {
  //  for (int i = 0; i < args.length; i++) {
    String filename = args.length < 1 ? "demo.lq" : args[0];
    try {
      LiquizCompiler c = new LiquizCompiler(filename);
      c.generateQuiz(filename);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}