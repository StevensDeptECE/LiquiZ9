import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class Variable extends QuestionType {
    private String name;

    @Override
    public void setText(final string body) {
      Matcher m;
      regex_search(body, m, parseVar);
      name = m[1];
      System.out.print("var " + name + "\n");
    }

    @Override
    public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
      if (vars.find(name) != vars.end()) {
        System.out.print("variable " + name + "==>" +  vars.at(name) + '\n'); //TODO: compiler->vars?
    
        return vars.at(name);
      }
      System.out.print("Undefined variable " + name + '\n');
      return "";
    }
}