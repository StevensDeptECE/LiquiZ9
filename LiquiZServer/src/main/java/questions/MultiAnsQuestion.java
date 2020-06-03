/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 *
 * @author ejone
 */
public class MultiAnsQuestion extends Question {
  boolean subWrongAns;
  String[] answers;

  public MultiAnsQuestion(String ans[], double gradeVal, boolean subWrongAns, HashMap<String, Question> questionsMap, String name) {
    super(gradeVal, questionsMap, name);
    this.subWrongAns = subWrongAns;
    answers = ans;
  }

  public String[] getAnswer() {
    return answers;
  }

  public boolean getSubWrongAns() {
    return subWrongAns;
  }
  
//TODO: add posibility to subtract points if one submitted is wrong 

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
  @Override
  public double checkAnswer(String studentAns[]) {
    double finishedGradeVal = 0.0;
    final double pointsPerAnswer = gradeVal/answers.length;
    for(String a : studentAns){
      int index = 0;
      for(String answer : answers) {
        index++;
        if(answer.equals(a)){
          finishedGradeVal += pointsPerAnswer;
          break;
        }
        else if(index == answers.length && subWrongAns)
          finishedGradeVal -= pointsPerAnswer;
      }
    }
    return Math.round(finishedGradeVal*1000)/1000;
  }
}
