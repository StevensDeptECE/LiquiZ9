package edu.stevens;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //these are just test variables for now
    private int questionNumber = 1;
    private int partNumber = 1;
    private String defaultText = "default text";
    private String answerText = "Answer text";
    private double points = 1.0;

    public String expectLine(String message) throws Exception {
        String line;
        do {
            line = br.readLine();
            if (line == null) {
                //throw new ParseException("Expected line" + lineNumber + message);
                lineNumber++;
                return line;
            }
        } while (line.startsWith("#"));
        // if line starts with # it's a comment, just return
        lineNumber++;
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
    public boolean isJSONValidQuiz(String test) {
        try {
            gson.fromJson(test, QuizSpecInclude.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
    public boolean isJSONValidQuestionContainer(String test) {
        try {
            gson.fromJson(test, QuestionContainer.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
    public void questionType(String line, QuestionContainer q) {
        Pattern essayQuestion = Pattern.compile("\\$eq:[^\\$]*\\$");
        Pattern numberFillInQuestion = Pattern.compile("\\$f[^\\$]+\\$");
        Pattern fillInQuestion = Pattern.compile("\\$f[^\\$]+\\$");
        Pattern horizontal = Pattern.compile("\\$mch:[^\\$]*\\$");
        Pattern vertical = Pattern.compile("\\$mcv:[^\\$]*\\$");
        Pattern video = Pattern.compile("\\$vid:[^\\$]*\\$");
        Matcher eqm = essayQuestion.matcher(line);
        Matcher nfqm = numberFillInQuestion.matcher(line);
        Matcher fqm = fillInQuestion.matcher(line);
        Matcher hm = horizontal.matcher(line);
        Matcher vm = vertical.matcher(line);
        Matcher v = video.matcher(line);
        boolean eqmatches = eqm.matches();
        boolean nfqmatches = nfqm.matches();
        boolean fqmatches = fqm.matches();
        boolean hmatches = hm.matches();
        boolean vmatches = vm.matches();
        boolean videomatches = v.matches();


        if (eqmatches == true){
            q.add(new EssayQuestion(questionNumber, partNumber, points, defaultText, answerText));
            partNumber++;
        }
//        if (nfqmatches == true) {
//            q.add(new NumberFillinQuestion());
//        }
        if (fqmatches == true) {
            q.add(new FillinQuestion(questionNumber, partNumber, points, answerText));
            partNumber++;
        }
//        if (hmatches == true) {
//            q.add(new HorizontalMultipleChoiceQuestion());
//        }
//        if (vmatches == true) {
//            q.add(new VerticalMultipleChoiceQuestion());
//        }
//        if (videomatches == true){
//           q.add(new Video());
//        }
    }
    public Quiz expectQuizJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        //String line = expectLine("did not find quiz json, bail or try to go on?");
        String json = expectLine("Expect JSON");
        final QuizSpecInclude qsi = gson.fromJson(json, QuizSpecInclude.class);
        //String qSpecFileJson = Files.readString(Paths.get("resources/"+qsi.qspec));

        String qSpecFileJson = readFile("src/main/resources/"+qsi.qspec);
        final QuizSpec qs = gson.fromJson(qSpecFileJson, QuizSpec.class);
        // if you successfully parse out json, return a new Quiz configured
        // gson.fromJSON(...);
        return new Quiz(qsi, qs); //TODO: configure
    }
    public QuestionContainer findNextQuestionContainerJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        String line = expectLine("Expect JSON");
        boolean jsonValue = isJSONValidQuestionContainer(line);
        //We have to have this if statement because it represents if we have reached the end of the quiz of not
        //It ends the while loop down below, which allows generateHTML() to run
        if (line == null) {
            return null; //
        }
        //this line checks to see if a line is json or not
        if (jsonValue == false) {
            while (jsonValue == false) {
                line = expectLine("Expect JSON");
                jsonValue = isJSONValidQuestionContainer(line);
                if (jsonValue == true) {
                    break;
                }
            }
            final QuestionContainerSpec qcs = gson.fromJson(line, QuestionContainerSpec.class);
            return new QuestionContainer(qcs);
        }
        else {
            final QuestionContainerSpec qcs = gson.fromJson(line, QuestionContainerSpec.class);
            return new QuestionContainer(qcs);
        }
    }
    public void addToQuestion(QuestionContainer q) throws Exception{
        //this function adds the questions of a question container to the question container
        while (true) {
            String line = expectLine("Expected Question"); // either #comment (throw out) or text (add to question) or "---"
            Pattern p = Pattern.compile("\\$[^\\$]+\\$");
            Matcher m = p.matcher(line);
            boolean matches = m.matches();;
            if (line.equals("---"))
                break;
            if(matches == true) {
                questionType(line, q);
            }
            else {
                q.add(new Text(line));
            }
        }
        //this part of the code then adds the question container to the quiz
        //it does not need to return anything
        quiz.add(q);
        partNumber = 1;
        questionNumber++;
    }
    public void generateHTML() throws IOException{
        quiz.writeHTML();
        System.out.println(quiz.getHTML());
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
            QuestionContainer q = findNextQuestionContainerJSON();
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
