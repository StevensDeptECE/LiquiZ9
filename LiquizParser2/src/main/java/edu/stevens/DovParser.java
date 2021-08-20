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
            if (line.equals("---"))
                break;
            //See if you can check if line is only text
            //Maybe see if line does not contain pattern $...$
            q.add(new Text(line));
        }
        //this part of the code then adds the question container to the quiz
        //it does not need to return anything
        quiz.add(q);
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
