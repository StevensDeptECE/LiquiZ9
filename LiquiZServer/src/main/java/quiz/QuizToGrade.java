/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quiz;

/**
 *
 * @author ejone
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import questions.Question;
import questions.NumQuestion;
import questions.SimpleQuestion;
import questions.MultiAnsQuestion;

public class QuizToGrade{
  HashMap<String, String[]> inputs;
  HashMap<String, Question> questionsMap;
  String filename;
  double grade;
  boolean quizGraded;

  public QuizToGrade(String filename, HashMap<String,String[]> inputsMap){
    this.grade = 0.0;
    this.quizGraded = false;
    this.filename = filename;
    this.inputs = inputsMap;
    questionsMap = new HashMap<>();
    updateAnswers(filename);
    updateGrade();
  }

  private void updateAnswers(String filename){
    try {
      File myObj = new File(filename);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String qType = myReader.next();
        double gVal = myReader.nextDouble();
        String qAnsLine = myReader.nextLine();
        String qAns = qAnsLine.trim();
        System.out.println(qType + ", " + qAns);

        addQuestion(qType, qAns, gVal);
      }
      myReader.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("An error occurred reading in the file.");
    }
  }

  public void addQuestion(String qType, String qAns, Double gradeVal){
      System.out.println(qType);
      System.out.println(qType.charAt(0));
      String[] qAnsArr = qAns.split(",");
    switch(qType.charAt(0)){
      case 'q':
      case 'Q':
      case 's': 
      case 'S':
        SimpleQuestion sqc = new SimpleQuestion(qAns, gradeVal, qType);
        questionsMap.put(qType, sqc);
        System.out.println("add question");
        break;
      case 'N':
      case 'n':
        double lowVal = Double.parseDouble(qAnsArr[0]);
        double highVal = Double.parseDouble(qAnsArr[1]);
        NumQuestion nq = new NumQuestion(lowVal, highVal, gradeVal);
        questionsMap.put(qType, nq);
        break;
      case 'M':
      case 'm':
        MultiAnsQuestion mqc = new MultiAnsQuestion(qAnsArr, gradeVal, true);
        questionsMap.put(qType, mqc);
        break;
      default:
        System.out.println("error adding question");
    }
  }

  private void updateGrade(){
    for(Map.Entry<String, String[]> entry : inputs.entrySet()){
      String key = entry.getKey();
      String[] inputsString = entry.getValue();
      System.out.println(key + " " + inputsString[0]);
      
      Question q = questionsMap.get(key);
      if(q==null)
         System.out.println("Null key: " + key);
      else
        grade+=q.checkAnswer(inputsString);
    }
    quizGraded = true;
  }

  public double getGrade(){
    return grade;
  }

  public boolean isGraded(){
    return quizGraded;
  }
}