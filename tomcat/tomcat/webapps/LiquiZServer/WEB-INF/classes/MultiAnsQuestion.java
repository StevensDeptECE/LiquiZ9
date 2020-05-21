package multiansquestion;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Enumeration;
import java.util.ArrayList;
import package.question;

public class MultiAnsQuestion {
  String type;
  int numAns;
  Double gradeVal;
  Boolean caseSens;
  ArrayList<String> answers = new ArrayList();

  public MultiAnsQuestion(String ans[], Double gradeVal, Boolean case){
    this.type = "MultiAnsQuestion";
    this.gradeVal = gradeVal;
    this.numAns = 0;
    this.caseSens = case;

    for(String a : ans){
      answers.add(a);
      this.numAns+=1;
    }
  }

  public String getType(){
    return type;
  }

  public double getGradeValue(){
    return gradeVal;
  }

  public ArrayList<String> getAnswer(){
    return answers;
  }

  public getCaseSensitivity(){
    return caseSens;
  }

  public getNumberOfAnswers(){
    return numAns;
  }

  public Double checkAnswer(String studentAns[]){
    int i = 0;
    Double finishedGradeVal = 0.0;
    for(String a : answers){
      String studentA = studentAns[i];
      if(!caseSens){
         = studentAns[i].toLowerCase();
        a = a.toLowerCase();
      }
      if(lCaseStudentAns.compare(a)==0)
        finishedGradeVal += (1/numAns)*gradeVal;
    }
    return finishedGradeVal;
  }
}
