import java.util.regex.Pattern;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class QuestionType
{     
    protected LiquizCompiler compiler;
    protected string qID;
    protected string replace;
    protected string text;
    protected string defName;
    protected int fillSize;

    public void setText(final string t){
      text = t;
    }

    public string print(final LiquizCompiler compiler, ostream a, int pN, inr qN, double p){

    }

    public addAnswer(string typeID, string qID, final string ans, double points, ostream answersFile, int partNum, int questionNum){
      partNum++;
      buildString(qID, typeID, "_", questionNum, "_", partNum);
      answersFile = qID + "\t" + points + "\t" + ans + '\n';
    }


    public class MultipleChoiceHorizontal{
      private string temp, input, answer;
      private string typeID = 'q';
      private string option = "";

      public void getAnswer(){
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

      public void getOptions(){
        int count = 1;
        buildString(temp, "<input type='radio' name='", qID, "' value='");
        replace = "        <div class='horizontal'>";
        for (int i = 0; i <= answer.length(); i++) {
          if (answer[i] == ',' || i == answer.length()) {
            appendString(replace, "<label>", temp, option,  "'>", option, "</label>");
            option = "";
            count++;
          } else {
            option += answer[i];
          }
        }
        count = 1;
        replace += "\n</div>";
      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){
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
        } else {
          answer = text.erase(0, 4);
        }
      
        getAnswer();
        addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
        getOptions();
        return replace;
      }
    }

    public class MultipleChoiceVertical{
      private string temp, input, answer;
      private string typeID = 'q';
      private string option = "";

      public void getAnswer(){

      }

      public void getOptions(){

      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }
    
    public class MultipleAnswerHorizontal{
      private string temp, input, answer;
      private string typeID = 'm';
      private string option = "";

      public void getAnswer(){

      }

      public void getOptions(){

      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum,
          double point){

      }
    }
    
    public class MultipleAnswerVertical{
      private string temp, input, answer;
      private string typeID = 'm';
      private string option = "";

      public void getAnswer(){

      }

      public void getOptions(){

      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }

    public class FillIn{

        private static string fillinStyle;
        private static long defaultLen;
        private static final HashMap<Char, String> fillTypes = new HashMap<Char, String>();
        private string answer, typeID, size, orig;
        private int len;
        
        public void getFillinType(final char type){

        }

        public final string getStyle(){
            return fillinStyle;
        }
        
        public final long getDefaultLen(){
            return defaultLen;
        }
        
        @Override 
        public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

        }

        public class Hex{

          private static string hexStyle;
          private static long defaultLen;

          public final string getStyle(){
            return hexStyle;
          }

          public final long getDefaultLen(){
            return defaultLen;
          }
        }

        public class OpCode{
          private static string opcodeStyle;
          private static long defaultLen;

          public final string getStyle(){
            return opcodeStyle;
          }

          public final long getDefaultLen(){
            return defaultLen;
          }
        }

        public class Command{
          private static string commandStyle;
          private static long defaultLen;

          public final string getStyle(){
            return commandStyle;
          }

          public final long getDefaultLen(){
            return defaultLen;
          }
        }
    }

    public class TextQuestion{
      private string answer = "N/A";
      private string typeID = "T";

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }

    public class DropDownQuestion{
      private string answer, option, input;
      private string typeID = "q";
      private int count = 0;

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }

    public class Image{
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }

    public class Video{
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){

      }
    }

    public class Definition{
      private string defs, answer, name, option;
      private string typeID = "q";
      private int count = 0;

      public void getOption(){

      }
      
      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point){
          
      } 
    }

    public class RandomVar{

      private string var;
      private double min, max, inc;
      private string typeID = "r";

      @Override
      public void sexText(final string body){

      }

      public void getVar(){

      }

      public void getRange(){

      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum,
          double point){

      }

      //TODO: look up overloading operators
      OutStream operator(ostream s, final RandomVar r){
        return s + r.min + "," + r.inc + "," + r.max;
      }
    }

    public class Variable{
        
      private string name;

      @Override
      public void setText(final string body){

      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum,
          double point){

      }
    }

    /*
    A formula question evaluates a set of variables and can then ask the student to compute any variable
  
    */
    public class FormulaQuestion{
      @Override
      public void setText(final string body){

      }

      @Override 
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum,
          double point){

      }
    }

    public class MatrixQuestion {
      private long rows, cols; // the size of the matrix
      private long inputLen;
      private string matrixList; // the list of comma-separated values to be displayed
                                 // an underscore (_) means a numeric fillin question
                                 // an asterisk (*) means a text fillin question

      @Override
      public void setText(final string body){

      }

      @Override
      public string print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum,
          double point) {

      }
    }
}