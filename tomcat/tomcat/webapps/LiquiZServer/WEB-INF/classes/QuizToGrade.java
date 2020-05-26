//package classes.quiztograde;
import java.io.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//import classes.question.*;
//import classes.numquestion.*;

public class QuizToGrade {
  HashMap<String, String[]> inputsMap = new HashMap<String, String[]>();
  HashMap<String, Question> questionsMap = new HashMap<String, Question>();
  Double grade;
  Boolean quizGraded;

  public QuizToGrade(String filename, Map inputsMap){
    this.grade = 0.0;
    this.quizGraded = false;
    inputsMap.putAll(inputsMap);

    updateAnswers(filename);
  }

  public void updateAnswers(String filename){
    try {
      File myObj = new File(filename);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String qType = myReader.next();
        String[] qAns = {myReader.nextLine()};
        addQuestion(qType, qAns, 0.0);
      }
      myReader.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("An error occurred reading in the file.");
      e.printStackTrace();
    }
  }

  public void addQuestion(String qType, String qAns[], Double gradeVal){
    switch(qType.charAt(0)){
      case 'q':
        SimpleQuestion sqc = new SimpleQuestion(qAns[0], gradeVal, true);
        //answers.add(q);
        questionsMap.put(qType, sqc);
        break;
      case 'Q':
        SimpleQuestion sq = new SimpleQuestion(qAns[0], gradeVal, false);
        questionsMap.put(qType, sq);
        break;
      case 'n':
        NumQuestion nq = new NumQuestion(0.0, 1.0, gradeVal);
        questionsMap.put(qType, nq);
        break;
      case 'm':
        MultiAnsQuestion mqc = new MultiAnsQuestion(qAns, gradeVal, true);
        questionsMap.put(qType, mqc);
        break;
      case 'M':
        MultiAnsQuestion mq = new MultiAnsQuestion(qAns, gradeVal, false);
        questionsMap.put(qType, mq);
        break;
    }
  }

  public void updateGrade(){
    for(Map.Entry<String, String[]> entry : inputsMap.entrySet()){
      String key = entry.getKey();
      String[] inputs = entry.getValue();

      grade+=questionsMap.get(key).checkAnswer(inputs);
    }
    quizGraded = true;
  }

  public Double getGrade(){
    return grade;
  }

}
