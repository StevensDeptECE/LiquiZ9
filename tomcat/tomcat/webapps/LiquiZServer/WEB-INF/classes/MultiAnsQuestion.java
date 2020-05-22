//package classes.multiansquestion;
import java.io.*;
import java.util.ArrayList;
//import classes.question.*;

public class MultiAnsQuestion extends Question{
  int numAns;
  Boolean caseSens;
  ArrayList<String> answers = new ArrayList<String>();

  public MultiAnsQuestion(String ans[], Double gradeVal, Boolean caseSensitivity){
    super(gradeVal);
    this.numAns = 0;
    this.caseSens = caseSensitivity;

    for(String a : ans){
      if(!caseSens)
        a = a.toLowerCase();
      answers.add(a);
      this.numAns+=1;
    }
  }

  public ArrayList<String> getAnswer(){
    return answers;
  }

  public Boolean getCaseSensitivity(){
    return caseSens;
  }

  public int getNumberOfAnswers(){
    return numAns;
  }

  public Double checkAnswer(String studentAns[]){
    int i = 0;
    Double finishedGradeVal = 0.0;
    for(String a : studentAns){
      if(!caseSens){
        a = a.toLowerCase();
      }
      if(answers.contains(a))
        finishedGradeVal += (1/numAns)*gradeVal;
    }
    return finishedGradeVal;
  }
}
