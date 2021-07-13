import java.io.*;

class ParseException extends Exception {
    public ParseException(String message) {}
}

public class DovParser {
    private BufferedReader br;
    private Quiz quiz;

    public String expectLine() throws Exception {
        String line;
        do {
            line = br.readLine();
            if (line == null)
                throw new ParseException("Expected line");
        } while (!line.startsWith("#"));
        // if line starts with # it's a comment, just return
        return line;
    }
    public Quiz expectQuizJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        String line = expectLine();
        if (line == null)
            throw new ParseException("did not find quiz json, bail or try to go on?");
        // if you successfully parse out json, return a new Quiz configured
        // gson.fromJSON(...);
        return new Quiz(); //TODO: configure
    }

    public Question findNextQuestionJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        String line = expectLine();
        if (line == null) {
            return null; // end quiz cleanly
        }
        // if you successfully parse out json, return a new question configured
        // gson.fromtJSON
        Question q = null; // new Question(); // if end of file, break out cleanly?
        return null; //TODO: new Question(); //TODO: configure the question
    }

    public void addToQuestion(Question q) {
  /*
        while (true) {

            String line = nextToken(); // either #comment (throw out) or text (add to question) or "---"
            if (line .eq ("---"))
                break;
            q.add(line);
        }

   */
       // quiz.add(q);
    }
    public void generateHTML() {
    }
    public DovParser(String liquiZFilename) throws Exception {
        // open file
        // read in 1st line --> convert using gson to Java Object
        //        QuizConf conf = new QuizConf();

        br = new BufferedReader(new FileReader(liquiZFilename));

        quiz = expectQuizJSON();
        while (true) {
            Question q = findNextQuestionJSON();
            if (q == null)
                break;
            addToQuestion(q);
        }
        generateHTML();
    }
//        generateAnswers();
//        generateXML();

/*
    // first we expect to see 1 line of json (explains the question)
    case: bunch of text, no formatting



 */
}
