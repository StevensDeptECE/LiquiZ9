import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.*;

public class RandomVar extends QuestionType {
    private String var;
    private double min, max, inc;
    private String typeID;

    public RandomVar() {
        typeID = "r";
    }

    @Override
    public void setText(String body) {
        System.out.print("Random var " + body + "\n");
        Matcher m;
        regex_search(body, m, parseRandomVar);
        System.out.print("matches:" + m[1] + "," + m[2] + "," + m[3] + "," + m[4] + '\n');
        String varName = m[1];
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
        for (int i = 1; text.charAt(i) != '}'; i++) {
        var += text.charAt(i);
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
    public String print(final LiquizCompiler compiler, OutputStream answerFile, int partNum, int questionsNum, double point) {
        text.erase(0, 3);
        getVar();
        text.erase(0, var.length() + 3);
        if (text.charAt(0) == '(') {
            getRange();
        }
        System.out.println(this + "\n");
        return "";
    }

    //TODO: look up overloading operators
    OutputStream operator(OutputStream s, final RandomVar r) {
        return s + r.min + "," + r.inc + "," + r.max;
    }
}