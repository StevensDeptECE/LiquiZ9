//package classes.quiztograde;
import java.io.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//import classes.question.*;
//import classes.numquestion.*;

public class QuizToGrade {
  ArrayList<String> studentInputs = new ArrayList<String>();
  ArrayList<Question> answers = new ArrayList<Question>();
  Double grade;
  Boolean quizGraded;

  public QuizToGrade(String filename){
    this.grade = 0.0;
    this.quizGraded = false;

    updateAnswers(filename);
  }

  public void updateAnswers(String filename){
    try {
      File myObj = new File(filename);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String qType = myReader.next();
        String qAns = myReader.nextLine();
        addQuestion(qType, qAns);
      }
      myReader.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("An error occurred reading in the file.");
      e.printStackTrace();
    }
  }

  public void addInputs(String input){
    studentInputs.add(input);
  }

  public void addQuestion(String qType, String qAns, Double gradeVal, Boolean caseSensitivity){
    switch(qType){
      case "SimpleQuestion":
        SimpleQuestion q = new SimpleQuestion(qAns, gradeVal, caseSensitivity);
        answers.add(q);
        break;
      case "NumQuestion":
        NumQuestion q = new NumQuestion(gradeVal);
        answers.add(q);
        break;
    }
  }

  public void updateGrade(){
    int i = 0;
    for(Question q : answers){
      String[] input = {studentInputs.get(i)};
      grade+=q.checkAnswer(input);
    }
    quizGraded = true;
  }

  public Double getGrade(){
    return grade;
  }


}
