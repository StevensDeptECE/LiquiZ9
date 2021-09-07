package edu.stevens;

import com.google.gson.Gson;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParseException extends Exception {
    public ParseException(String message) {}
}

public class LiquiZParser {
    private static final Gson gson;
    private static final Pattern questionPattern;
    private static final HashMap<String, QuestionFactory> questionTypes;
    private static final String defaultText;
    static {
        gson = new Gson();
        questionPattern = Pattern.compile("\\$(eq|f[nqQ]|mc[hvd]|vid|mat|aud)(\\{[^\\}]*\\})?:([^\\$]+(?:\\\\\\$[^\\$]+)*)\\$");
        questionTypes = new HashMap<>(64);
        questionTypes.put("mch", new HorizontalMultipleChoiceQuestionFactory());
        questionTypes.put("mcv", new VerticalMultipleChoiceQuestionFactory());
        questionTypes.put("mcd", new DropDownQuestionFacotry());
        questionTypes.put("fn", new NumberFillInQuestionFactory());
        questionTypes.put("fq", new FillInQuestionFactory());
        questionTypes.put("fQ", new FillInQuestionFactory()); //TODO: this needs to be case insensitive
        questionTypes.put("eq", new EssayQuestionFactory());
        questionTypes.put("mat", new MatrixQuestionFactory());
        defaultText = "Please input response here";
    }

    private String dir; // the directory where all inputs and outputs go
    private String quizName;
    private BufferedReader br;
    private Quiz quiz;
    private int lineNumber;
    private String schoolColor;
    private String schoolLogo;

    private int questionNumber;
    private int partNumber;

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
    public int getQuestionNumber() {
        return questionNumber;
    }
    public int getPartNumber() {
        return partNumber;
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
        Matcher qm = questionPattern.matcher(line);
        if (qm.matches()) {
            String questionType = qm.group(1);
            String parameters = qm.group(2);
            String answers = qm.group(3);
            QuestionFactory qf = questionTypes.get(questionType);
            if (qf == null) {
                System.out.println("Error creating question");
            }
            else {
                if (qm.start() != 0) {
                    q.add(new Text(line.substring(0, qm.start()-1)));
                }
                Question question = qf.makeQuestion(this, qm);
                if (question == null) {
                    System.out.println("Error creating question");
                } else {
                    q.add(question);
                    partNumber++;
                }
                q.add(new Text(line.substring(qm.end())));
            }
        }
    }

    /*
        Read the quizspec file, and if it contains a parent specification, recursively read in its parent
        Return the value of the quizspec with all ancestors read in.
     */
    private QuizSpec readQuizSpec(String filename) throws IOException {
        String qSpecFileJson = readFile(dir + filename);
        final QuizSpec qs = gson.fromJson(qSpecFileJson, QuizSpec.class);
        if (qs.parent == null) // if there is no parent
          return qs; //just return this quizspec

        final QuizSpec qsParent = readQuizSpec(qs.parent); // read in the parent quiz specification file
        //Given that there is a parent, override all values in the parent that are in the child and return the resulting object

        if (qs.department != null)
           qsParent.department = qs.department;
        if (qs.instructor != null)
            qsParent.instructor = qs.instructor;
        if (qs.logo != null)
            qsParent.logo = qs.logo;
        if (qs.color != null)
            qsParent.color = qs.color;
        for (Map.Entry<String, String[]> e : qsParent.def.entrySet()) {
            String key    = e.getKey();
            String[] val  = e.getValue();
            qsParent.def.put(key, val); // override all definitions redefined in child into parent
        }
        for (Map.Entry<String, Object> e : qsParent.defaults.entrySet()) {
            String key    = e.getKey();
            Object val  = e.getValue();
            qsParent.defaults.put(key, val); // same
        }
        return qsParent;
    }

    private Quiz expectQuizJSON() throws Exception {
        // if this line is not json, give error, find next line that is json
        //String line = expectLine("did not find quiz json, bail or try to go on?");
        String json = expectLine("Expect JSON");
        final QuizSpecInclude qsi = gson.fromJson(json, QuizSpecInclude.class);
        //String qSpecFileJson = Files.readString(Paths.get("resources/"+qsi.qspec));

        final QuizSpec qs = readQuizSpec(qsi.qspec);
        // if you successfully parse out json, return a new Quiz configured
        // gson.fromJSON(...);
        return new Quiz(qsi, qs);
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
            Matcher m = questionPattern.matcher(line);
            if (m.matches()) {
                String questionType = m.group(1);
                String parameters = m.group(2);
                String answers = m.group(3);
                QuestionFactory qf = questionTypes.get(questionType);
                if (qf == null) {
                    System.out.println(lineNumber + " Unknown quenstion type");
                } else {
                    if (m.start() != 0) {
                        q.add(new Text(line.substring(0, m.start() - 1)));
                    }
                    Question question = qf.makeQuestion(this, m);
                    if (question == null) {
                        System.out.println("Error creating question");
                    } else {
                        q.add(question);
                        partNumber++;
                    }
                    if (m.end() < line.length())
                        q.add(new Text(line.substring(m.end())));
                }
            }
            //For this if statement see whether or not there is text with the given $...$ tag
            //If there is split the text fragments into a string array and return need functions
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
        PrintWriter pw = new PrintWriter(new FileWriter(dir + "output/" + quizName + ".html"));
        pw.println(quiz.getHTML());
        pw.close();
    }
    public void generateAnswers() throws IOException {

    }
    public LiquiZParser(String dir, String liquiZFilename) throws Exception {
        this.dir = dir + "/";
        quizName = liquiZFilename.substring(0, liquiZFilename.lastIndexOf("."));
        // open file
        // read in 1st line --> convert using gson to Java Object
        //        QuizConf conf = new QuizConf();
        questionNumber = 1;
        partNumber = 1;
        lineNumber = 0;
        br = new BufferedReader(new FileReader(this.dir + liquiZFilename));

        quiz = expectQuizJSON();
        while (true) {
            QuestionContainer q = findNextQuestionContainerJSON();
            if (q == null)
                break;
            addToQuestion(q);
        }
        generateHTML();
        generateAnswers();
    }
//        generateXML();

/*
    // first we expect to see 1 line of json (explains the question)
    case: bunch of text, no formatting



 */
}
