package edu.stevens;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ParseException extends Exception {
    public ParseException(String message) {}
}

public class DovParser {
    private BufferedReader br;
    private Quiz quiz;
    private int lineNumber;
    private Gson gson = new Gson();
    private String schoolColor;
    private String schoolLogo;

    public String expectLine(String message) throws Exception {
        String line;
        do {
            line = br.readLine();
            if (line == null)
                throw new ParseException("Expected line" + lineNumber + message);
        } while (line.startsWith("#"));
        // if line starts with # it's a comment, just return
        return line;
    }
    public void transformQuizSpec(QuizSpec qs) {
        for (Map.Entry<String, String[]> entry : qs.def.entrySet()) {
            String key = entry.getKey();
            try {
                String[] value = (String[]) entry.getValue();
                for (String v: value) {
                    System.out.println(v);
                }
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
    }
    private String readFile(String filename) throws IOException{
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return new String(data, "UTF-8");
    }
    public Quiz expectQuizJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        //String line = expectLine("did not find quiz json, bail or try to go on?");
        String json = expectLine("Expect JSON");
        final QuizSpecInclude qsi = gson.fromJson(json, QuizSpecInclude.class);
        //String qSpecFileJson = Files.readString(Paths.get("resources/"+qsi.qspec));

        String qSpecFileJson = readFile("src/main/resources/"+qsi.qspec);
        final QuizSpec qs = gson.fromJson(qSpecFileJson, QuizSpec.class);
        //transformQuizSpec(qs);
        //schoolColor = schoolInformationColor(qsi, qs);
        //schoolLogo = schoolInformationLogo(qsi, qs);
        // if you successfully parse out json, return a new Quiz configured
        // gson.fromJSON(...);
        return new Quiz(qsi, qs); //TODO: configure
    }

    public QuestionContainer findNextQuestionJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        String line = expectLine("message");
        if (line == null) {
            return null; // end quiz cleanly
        }
        // if you successfully parse out json, return a new question configured
        // gson.fromtJSON
        QuestionContainer q = new QuestionContainer(); // new Question(); // if end of file, break out cleanly?
        return null; //TODO: new Question(); //TODO: configure the question
    }

    public void addToQuestion(QuestionContainer q) {
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
    public void generateHTML() throws IOException{
        quiz.writeHTML();
        System.out.println(quiz.getHTML());
        System.out.println(EssayQuestion.class);
        PrintWriter pw = new PrintWriter(new FileWriter("Quiz.html"));
        pw.println(quiz.getHTML());
        pw.close();
    }
    public DovParser(String liquiZFilename) throws Exception {
        // open file
        // read in 1st line --> convert using gson to Java Object
        //        QuizConf conf = new QuizConf();
        lineNumber = 0;
        br = new BufferedReader(new FileReader(liquiZFilename));

        quiz = expectQuizJSON();
        while (true) {
            QuestionContainer q = findNextQuestionJSON();
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
