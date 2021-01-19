import java.util.regex.Pattern;
import java.util.regex.Matcher;
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

    see: https://devqa.io/how-to-parse-json-in-java/
*/

public class LiquizCompiler
{
    static final string emptystr;
    static final string escapedDollar;
    static final string defaultQuiz;
    static final long defaultFillInBlankSize; // (long is used as an unsigned integer)

    private static final Pattern questionStart;
    private static final Pattern specials;
    private static final Pattern qID;
    static Pattern questionPattern;

    private static long uuid; // (long is used as an unsigned integer)

    private static final HashMap<String, QuestionType> questionTypes = new HashMap<String, QuestionType>();
    private Map<String, String> variables;
    private HashMap<String, String> definitions = new HashMap<String, String>();

    //class Definition {}
    private final string DELIM = "---";
    private string questionText;
    private string inputText;
    private string answerText;
    private string answerInput;

    private string outputDir; // directory where the public output goes (html)
    //TODO: need to generate randomized numbers for the images so that file names do not 
    //give away answers and place in the output directory

    private OutputStream html;
    private OutPutStream answers;
    private OutputStream xml;
    private InputStream liquizFile;

    private long cursor; // byte offset into quiz file (long is used as an unsigned integer)
    private char bytes; // underlying bytes in quiz
    private long fileSize; // size of quiz text in bytes (long is used as an unsigned integer)
  
    private QuestionType defaultQuestionType; //TODO write questiontype object
    
    private int logLevel;           // verbose level, how much debugging to display
    private int questionNum;        // the number of the current question
    private int partNum;            // the subnumber within each question
    private int lineNumber;         // line number within the .lq file
    //private int questionLineNumber; // 
    private double questionCount;   // number of question inputs in the current question
    private double points;          // total number of points in the quiz
    private int fillSize;           // default number of characters in a fill-in question
    private int timeLimit;          // number of minutes to take the quiz, 0 means untimed
  
    private string imgFile, styleSheet, quizName, license, copyright, author, email;
        
    static {
      // ideally everything you do to initialize the class should be in here
      questionStart = Pattern.compile("^\\{");
      specials = Pattern.compile("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");
      qID = Pattern.compile("name='[q||T||Q||m||s||n||S]_[0-9]*_[0-9]*'");
      questionPattern = ("\\$(?:mch|mcv|dro|mah|mav|fQ|fq|fn|fs|fS)\\$");

      uuid = 1;

      emptystr = "";
      escapedDollar = "\\$";
      defaultQuiz = "quiz.css";
      defaultFillInBlankSize = 6;
    }

    private void setLogLevel(int level){
      logLevel = level; 
    }
    
    private void findQuestionType(final string type, double points, string delim, int pos, int len){
      QuestionType question = (questionTypes.find(type) != questionTypes.end());
      if (question != nullptr){
        question = setText(delim);
        inputText = question.print(this, answers, partNum, questionNum, points);
        questionText.replace(pos, len, inputText);
      }
    }

    private string removeExtension(final char fileName[]){
      int i;
      for (i = 0; fileName[i] != '\0'; i++);
      for (; i >= 0 && fileName[i] != '.'; i--);
      return string(fileName, i + 1);
    }

    private void findDefinitions(final string name, string defs){
      if (definitions.find(name) == definitions.end()){
        System.out.println("missing definition " + name + " on line " + questionLineNumber);
      } 
      else {
        defs = definitions.at(name);
      }
    }

    private void includeQSpec(JSONObject parentQuizSpec, final string filename){

      inputStream specFile = (("spec/" + filename).c_str());
      char dirName[];
      System.err.println( "Current directory: " + getcwd(dirName, sizeof(dirName)) + '\n');
      System.err.println( "filename: " + "spec/" + filename);
      if (!specFile.good()) {
        System.err.println( "Cannot open file " + filename);
      }
      JSONObject specInfo = parse(specFile);

      if (logLevel >= 3) {
        System.err.println( "dumping qspec json before merge");
        for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
          System.err.println( i.key() + "==>" + i.value());
      }

      if (specInfo.find("parent") != specInfo.end()) {
        includeQSpec(parentQuizSpec, specInfo.at("parent")); //TODO: merge specInfo on top of parentQuizSpec
        //TODO: merge isn't working, so comment out?
        merge(parentQuizSpec, specInfo);
        specInfo = parentQuizSpec;
      }
      if (parentQuizSpec != nullptr) {// only the first level sets all the variables below
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
          string name = it.key();
          string defs;
          for (int i = 0; i < it.value().size(); i++) {
            string defVal = it.value()[i];
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

    private void getJSONHeader(){
      string line;
      string specName;
      if (!getline(line)) {
        System.err.println("Unexpected end of file line while getting JSON header");
        return;
      }
      JSONObject header = parse(line);
      #if 0
        if (expect(specName, this, header, "quizspec")) {
          includeQSpec(specName);
        }
      #endif
        if (header.find("quizspec") != header.end()) {
        specName = header.at("quizspec");//TODO: pull error checking into separate function above
        includeQSpec(nullptr, specName);
    }  
  quizName = header.at("name"); //TODO: error checking
    }

    private void setAnswer(){
       Matcher m;
       answerText = questionText;
       int pos, end;
       while (find(answerText, m, qID)){
        pos = m.position();
         
         for (int i = m.position(); answerText[i] != '='; i++){
           pos++;
          }
        answerText.insert(pos+2, "a");
      }
    }

    private void generateHeader(){
      
     //TODO Translate raw string to java
      /*
      getJSONHeader();
        html << 
      R"(
    <!DOCTYPE html>
      <html>
      <head>
        <meta charset="UTF-8"/>
        <link rel="stylesheet" type="text/css" href='css/)" <<
        styleSheet <<
      R"('>
        <title>
      LiQuiz [)" <<
        quizName <<
      R"(]
        </title>
        <script src='js/quiz.js'></script>
        </head>
      <body onload='startTime()" <<
      timeLimit << ")'>" <<
    R"(
      <form method="get" action="/Grade"></form>
        <!-- Header -->
      <div id='header' class='header'>
        <img class='logo' src='media/)" <<
      imgFile << "'/>" <<
    R"(
      <div class='headerText'>
        <div class='quizTitle'>
          )" <<
      quizName <<
    R"(
      </div>
      <div class='headerDetails'>
        <div class='headerRow'>
          )" <<
      author <<
    R"(
        </div>
        <div class='headerRow'>
          Email  )" <<
           email << "  if you have any questions!" <<
    R"(
        </div>
          <div class='headerRow'>
            <input id='pledge' type='checkbox' name='pledged' value='pledged'/>
            <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>
          </div>
          <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>
          <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
        </div>
        </div>
        <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>
      </div>
    )";
    */
    }
    
    private void makeQuestion(JSONObject question){
      string style;
      require(question, "style", style, lineNum);
      string preStart, preEnd;
        if (style == "pcode" || style == "code") {
          preStart = "<pre class='" + style + "'>";
          preEnd = "</pre>";
      } else {
          preStart = "<pre class='text'>";
          preEnd = "</pre>";
        }
      if (style != "def") {
      //    string temp = question.at("points");
      double totalPoints = lookup(question, "points", 0, lineNum);
      string questionName = lookup(question, "name", emptystr, lineNum);
      Matcher m;
      final string end = "</p>";
      partNum = 0;
      while (find(questionText, m, specials)) {
        string delim = m[2];

        int pos = questionText.find("<p hidden>", m.position());
        int endPos = questionText.find(end, m.position());
        long questionLineNum = stoi(questionText.substr(pos+10, endPos-pos-10+1)); //TODO: get rid of this!

        string type;
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
        
        //TODO Translate raw string to java
        /*html << R"(
      <div class='section'>
        <div class='question' id='q)";
        html << questionNum << "'>";
        html << R"(
          <div>
            )";
        html << questionNum << "." << "\t" << questionName;
        html << R"(
            <span class='pts'>  )";
        html << "(" << totalPoints<< " points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>";
        html << R"(
          </div>
          )";
        html << preStart << endl;
        html << questionText << preEnd;

        html << R"(
        </div>

        <div class='answer'>
          )";
        html << preStart;
        html << R"(
    )";
        html << answerText << preEnd;
        html << R"(
        </div>
      </div>
      
      )";*/
      
        questionNum++;
      } else {
        string defs = lookup(question,"values", emptystr, lineNum);
        string name = lookup(question,"name", emptystr, lineNum);
        definitions[name] = defs;
        answers = "defs"+"\t" + name + "\t" + defs +'\n';
      }
    }

    
    private void grabQuestions(){
      string line, qID, temp;
      Matcher m;
      lineNumber = 1;
      while (getline(liquizFile, line) != liquizFile.eof()){
        if (find(line, m, questionStart)){  // looking for the beginning of a question
          istringstream s(line);
          JSONObject question;  // gets the question header
          s = question;
          lineNumber++;
          
          while (getline(liquizFile, line) != liquizFile.eof() && line != DELIM){  // gets line within question section
            lineNumber++;
            questionText = questionText + line + "<p hidden>" + to_string(lineNumber) + "</p>";
            questionText += '\n';
          }
          lineNumber++;
          for (int i = 0; i < questionText.length(); i++){
            if (questionText[i] == '$'){
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
    
    private void generateFooter(){
      String html = scanf(
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
    
    private void readFile(final char filename[], char bytes, long fileSize){ // (long is used as an unsigned integer)

    }

    private bool getline(string line){

    }

    private void QuestionHashMap()
    {
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

    //this is the constructor for Liquiz compiler class
    public LiquizCompiler(String filename){

      // style: everything you do to initialize object should be in here
    }
      
    public final int getLineNumber(){
      return lineNumber;
    }

    public void generateQuiz(final char liquizFileName[]){
      generateHeader();
      grabQuestions();
      generateFooter();
      closeFile();
    }
    
    public void dumpVariables(){
	    for (auto def : variables) { 
		    System.out.print(def.first + "\t==>\t" + def.second + '\n');
	    }
    }

    public static < T > T lookup(JSONObject json, final string key, final T defaultVal, int lineNum) {
      try {
        auto it = json.find(key);
        if (it != json.end())
          return T(it.value());
        } catch (Exception e) {
          System.out.println( "Error parsing json value "+ key + " of type " + nameof(defaultVal) + e.what());
        }
      return defaultVal;
    }

    public static < T > void require(JSONObject json, final string key, T target, int lineNum) {
      try {
        auto it = json.find(key);
        if (it != json.end())
          target = it.value();
        } catch (Exception e) {
          System.out.println("Expected " + key + " at line " + lineNum);
      }
    }

    public string nameof(string a) {
      return "string";
    }
    
    public string nameof(int a) {
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
    
    public static string name(int a) {
      return "int";
    }
}