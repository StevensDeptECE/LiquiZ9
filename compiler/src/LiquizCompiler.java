import java.util.regex.Pattern;
/**
  @author: 
  @author:
  @author: Dov Kruger
 
    see: https://devqa.io/how-to-parse-json-in-java/
  */
public class LiquizCompiler {
    private static final Pattern questionStart;


    private int x;

    static {
        // ideally everything you do to initialize the class should be in here
        questionStart = Pattern.compile("^\\{");


    }


    private StringBuilder  quizCode; // the code for the current quiz



   public LiquizCompiler(String filename) {
       x = 2; // style: everything you do to initialize object should be in here
   }


}