import java.util.regex.Pattern;
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

//this class needs to be put into a new file
//public class QuestionType {}  

public class LiquizCompiler
{
    private static final Pattern questionStart;
    private static final Pattern specials;
    private static final Pattern qID;

    private static long uuid; // (long is used as an unsigned integer)

    private static final HashMap<String, QuestionType> questionTypes = new HashMap<String, QuestionType>();
    private Map<String, String> variables;
    private HashMap<String, String> definitions;

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
  
    QuestionType defaultQuestionType; //TODO write questiontype object
    
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

      uuid = 1;
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
      for (i = 0; fileName[i] != '\0'; i++)
      ;
      for (; i >= 0 && fileName[i] != '.'; i--)
      ;
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

    }

    private void getJSONHeader(){

    }

    private void setAnswer(){
       smatch m;
       answerText = questionText;
       int pos, end;
       while (regex_search(answerText, m, qID)){
        pos = m.position();
         
         for (int i = m.position(); answerText[i] != '='; i++){
           pos++;
          }
        answerText.insert(pos+2, "a");
      }
    }

    private void generateHeader(){
    
    }
    
    private void makeQuestion(JSONObject question){

    }
    
    private void grabQuestions(){
      string line, qID, temp;
      smatch m;
      lineNumber = 1;
      while (getline(liquizFile, line), !liquizFile.eof()){
        if (regex_search(line, m, questionStart)){  // looking for the beginning of a question
          istringstream s(line);
          JSONObject question;  // gets the question header
          s = question;
          lineNumber++;
          
          while (getline(liquizFile, line),!liquizFile.eof() && line != DELIM){  // gets line within question section
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
      String html = 
        "<div class='footer'>" +
        "<pre id='bottomTime'></pre>" +
        "<input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>" +
        "</div>" + 
        "</form>" +
        "</body>" +
        "</html>";
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
		    System.out.print(def.first);
		    System.out.print("\t==>\t");
		    System.out.print(def.second);
		    System.out.print('\n');
	    }
    }
}