/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.HashMap;

/**
 *
 * @author ejone
 */
public class NumQuestion extends Question {
  private double low, high;
  
  public NumQuestion(double low, double high, double gradeVal, HashMap<String, Question> questionsMap, String name) {
    super(gradeVal, questionsMap, name);
    if(low < high){
        this.low = low;
        this.high = high;
    }
    else{
        this.low = high;
        this.high = low;        
    }
  }

  public double[] getAnswer() {
    return new double[]{low, high};
  }

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     * @throws NumberFormatException    String cannot be parsed into a double
     */
    @Override
  public double checkAnswer(String studentAns[]) throws NumberFormatException {
    try {
        double numStudentAns = Double.parseDouble(studentAns[0]);
        if(numStudentAns >= low && numStudentAns <= high)
            return 1.0*gradeVal;
    }
    //TODO: display on the client something went wrong
    catch (NumberFormatException nfe) {
        System.out.println("error, not a valid number");
    }
    return 0.0;
  }
}
