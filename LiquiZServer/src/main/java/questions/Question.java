/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.HashMap;

public abstract class Question {
  double gradeVal;

  public Question(double gradeVal, HashMap<String, Question> questionsMap, String name) {
    this.gradeVal = gradeVal;
    questionsMap.put(name, this);
  }

  public final String type() {
    return getClass().getSimpleName();
  }

  public final double getGradeValue() {
    return gradeVal;
  }

  public abstract double checkAnswer(String studentAns[]);

}
