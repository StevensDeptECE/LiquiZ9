package quiztograde;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import package.question;
import package.numquestion;

public class QuizToGrade {
  ArrayList<String> studentInputs = new ArrayList<String>();
  ArrayList<Question> answers = new ArrayList<Question>();
  Double grade;
  Boolean quizGraded;

  public quizToGrade(String filename){
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

  public void addQuestion(String qType, String qAns){

  }

  public void updateGrade(){
    int i = 0;
    for(Question q : answers){
      String input = studentInputs.get(i);
      grade+=q.getGradeValue();
    }
    quizGraded = true;
  }

  public Double getGrade(){
    return grade;
  }


}
