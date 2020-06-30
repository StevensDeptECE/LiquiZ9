/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import questions.MultiAnsQuestion;
import questions.NumQuestion;
import questions.Question;
import questions.SimpleQuestion;

/**
 *
 * @author ejone
 */
public class Quiz {
    private ObjectId mongoId;
    private String answerFile;
    private String quizId;
    private String classId;
    private int numTries;
    private double maxGrade;
    private Date showAnswersAfter;
    private TreeMap<Integer, Question> questionsMap;
    
    //TODO: implement numtries

    /**
     * Used to create a quiz
     * @param quizId value for id of the quiz
     * @param classId id of the class this quiz belongs to
     * @param answerFile name of the answer file to be used
     * @param numTries the number of tries a student has for this quiz
     * @param showAnswersAfter date to show the answers after
     */
    public Quiz(String quizId, String classId, String answerFile, int numTries, Date showAnswersAfter){
        this.quizId = quizId;
        this.classId = classId;
        this.answerFile = answerFile;
        this.numTries = numTries;
        this.showAnswersAfter = showAnswersAfter;
        questionsMap = new TreeMap<>();
        updateAnswers();
    }
    
    /**
     * Used to decode a quiz from the mongodb database
     * @param mongoId id of the quiz in the database
     * @param numTries the number of tries a student has for this quiz
     * @param quizId value for the id of the quiz
     * @param classId id of the class this quiz belongs to
     * @param answerFile name of the answer file that was used
     * @param maxGrade the max possible grade that could be received
     * @param showAnswersAfter date to show the answers after
     */
    public Quiz(ObjectId mongoId, int numTries, String quizId, String classId, String answerFile, double maxGrade, Date showAnswersAfter){
        this.mongoId = mongoId;
        this.numTries = numTries; 
        this.quizId = quizId;
        this.classId = classId;
        this.answerFile = answerFile;
        this.maxGrade = maxGrade;
        this.showAnswersAfter = showAnswersAfter;
        questionsMap = new TreeMap<>();
    }
    
    /**
     *
     * @return id of the quiz
     */
    public final String getQuizId() {
        return quizId;
    }
    
    /**
     *
     * @return id of the class this quiz belongs to
     */
    public final String getClassId() {
        return classId;
    }
    
    /**
     *
     * @return String for the answer file that is used
     */
    public final String getAnswerFile() {
        return answerFile;
    }
    
    /**
     *
     * @return ObjectId for the id of the quiz in the database
     */
    public final ObjectId getId() {
        return mongoId;
    }
    
    /**
     *
     * @return TreeMap<Integer,Question> for the question map of the quiz
     */
    public final TreeMap<Integer, Question> getQuestionsMap() {
        return questionsMap;
    }
    
    /**
     * Get the answers from each of the questions in the quiz
     * @return list of answers
     */
    public final ArrayList<String> getAnswers(){
        ArrayList<String> answersList= new ArrayList<>();
        for(Map.Entry<Integer, Question> entry : questionsMap.entrySet()){
            Question q = entry.getValue();
            answersList.add(q.getAnswer());
        }
        return answersList;
    }
    
    /**
     *
     * @return int for the number of possible tries
     */
    public final int getNumTries() {
        return numTries;
    }
    
    /**
     *
     * @return double of the max grade
     */
    public final double getMaxGrade() {
        return maxGrade;
    }
    
    /**
     *
     * @return Date when answers are released
     */
    public final Date getAnswersRelease() {
        return showAnswersAfter;
    }
    
    /**
     *
     * @param date new Date when answers should be released after
     */
    public final void setAnswersRelease(Date date) {
        showAnswersAfter = date;
    }
    
    /**
     *
     * @param newMaxGrade set the maxGrade of the quiz
     */
    public final void setMaxGrade(double newMaxGrade) {
        maxGrade = newMaxGrade;
    }
    
    /**
     *
     * @param newName set the quizId of the quiz
     */
    public final void setQuizId(String newName) {
        quizId = newName;
    }
    
    /**
     *
     * @param newId id of the new class
     */
    public final void setClassId(String newId) {
        classId = newId;
    }
    
    /**
     *
     * @param newFilename set a new answerFile to be used
     */
    public final void setAnswerFile(String newFilename) {
        answerFile = newFilename;
    }
    
    /**
     *
     * @param num new value for numTries
     */
    public final void setNumTries(int num) {
        numTries = num;
    }
    
    /**
     *
     * @param newId set the mongoId of the quiz
     */
    public final void setId(ObjectId newId) {
        mongoId = newId;
    }
    
    /**
     *
     * @return JSONArray which holds the string answers, double grade and id for each question
     * This is then passed to the html so it can display answers. 
     */
    public final JSONArray getJSON() {
        JSONArray jARR = new JSONArray();
        JSONObject jOBJ = new JSONObject();
        String currentQ = "";
        double currentGrade = 0.0;
        String answers = "";
        for(Map.Entry<Integer, Question> entry : questionsMap.entrySet()){
            Question q = entry.getValue();
            String newQ = q.getName().split("_")[1];

            if(currentQ.equals(newQ)){
                currentGrade+=q.getGrade();
                answers += "," + q.getAnswer();
                
            }
            else {
                currentQ = newQ;
                jOBJ.put("points", currentGrade);
                jOBJ.put("answers", answers);
                jARR.add(jOBJ);
                currentGrade = q.getGrade();
                answers = q.getAnswer();
                jOBJ = new JSONObject();
            }
        }
        jOBJ.put("points", currentGrade);
        jOBJ.put("answers", answers);
        jARR.add(jOBJ);
        return jARR;
    }
    
   
    private void updateAnswers(){
    try {
      File myObj = new File(answerFile);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String qType = myReader.next();
                double gVal = myReader.nextDouble();
                String qAnsLine = myReader.nextLine();
                String qAns = qAnsLine.trim();
                System.out.println(qType + ", " + qAns);
                
                maxGrade += Math.round(gVal*1000)/1000;
                addQuestion(qType, qAns, gVal);
            } 
            maxGrade = Math.round(maxGrade*1000)/1000;
        }  
        catch(InputMismatchException ex){
            System.out.println("An error occurred reading in the file.");
        }
    }
    //TODO: respond on server side, log it, major problem
    catch (FileNotFoundException e) {
      System.out.println("An error occurred reading in the file.");
    }
  }

    /**
     * Adds a question to the quiz's treemap questionsMap
     * @param questionName id of the question used to determine what type it is
     * @param qAns The answers for the question which are parsed differently. 
     * @param gradeVal The grade value of the question
     */
    public void addQuestion(String questionName, String qAns, Double gradeVal){
    String[] qAnsArr = qAns.split(",");
    switch(questionName.charAt(0)){
      case 'q':
      case 'Q':
      case 's': 
      case 'S':
        SimpleQuestion sqc = new SimpleQuestion(qAns, gradeVal, questionName, questionsMap);
        break;
      case 'N':
      case 'n':
        double lowRange = Double.parseDouble(qAnsArr[0]);
        double highRange = Double.parseDouble(qAnsArr[1]);
        NumQuestion nq = new NumQuestion(lowRange, highRange, gradeVal, questionsMap, questionName);
        break;
      case 'M':
      case 'm':
        MultiAnsQuestion mqc = new MultiAnsQuestion(qAnsArr, gradeVal, true, questionsMap, questionName);
        break;
      default:
        System.out.println("error adding question");
    }
  }
  
    /**
     *
     * @param q the question given to the quiz from Mongodb to add to questionsMap
     */
    public void addQuestion(Question q){
      String split[] = q.getName().split("_");
      questionsMap.put(Integer.parseInt(split[2]), q);
  }
  
  
    
}
