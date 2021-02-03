import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

/*
  A formula question evaluates a set of variables and can then ask the student to compute any variable
*/
public class FormulaQuestion extends QuestionType{
    @Override
    public void setText(final String body) {

    }

    @Override 
    public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
      System.out.print("formula\n");
      return "formula";
    }
}