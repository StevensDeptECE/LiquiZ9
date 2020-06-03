/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

public abstract class Question{
  double gradeVal;

  public Question(double gradeVal){
    this.gradeVal = gradeVal;
  }

  public final String type(){
    return getClass().getSimpleName();
  }

  public double getGradeValue(){
    return gradeVal;
  }

  public abstract double checkAnswer(String studentAns[]);

}
