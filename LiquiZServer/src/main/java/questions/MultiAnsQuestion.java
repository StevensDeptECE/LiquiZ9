/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.ArrayList;
import java.util.Arrays;
/**
 *
 * @author ejone
 */
public class MultiAnsQuestion extends Question{
  int numAns;
  boolean subWrongAns;
  ArrayList<String> answers = new ArrayList<>();

  public MultiAnsQuestion(String ans[], double gradeVal, boolean subWrongAns){
    super(gradeVal);
    this.subWrongAns = subWrongAns;
    numAns = ans.length;
    addAnswers(ans);
  }

  public ArrayList<String> getAnswer(){
    return answers;
  }

  public boolean getSubWrongAns(){
    return subWrongAns;
  }

  public int getNumberOfAnswers(){
    return numAns;
  }
  
  private void addAnswers(String ans[]){
      answers.addAll(Arrays.asList(ans));
  }
  
//TODO: add posibility to subtract points if one submitted is wrong 

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
  @Override
  public double checkAnswer(String studentAns[]){
    int i = 0;
    double finishedGradeVal = 0.0;
    for(String a : studentAns){
      if(answers.contains(a))
        finishedGradeVal += (1.0/numAns)*gradeVal;
      else if(subWrongAns)
        finishedGradeVal -= (1.0/numAns)*gradeVal;
    }
    return finishedGradeVal;
  }
}
