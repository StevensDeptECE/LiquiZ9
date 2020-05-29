/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

/**
 *
 * @author ejone
 */
public class NumQuestion extends Question {
  double[] ansRange = new double[2];

  public NumQuestion(double lowRange, double highRange, double gradeVal){
    super(gradeVal);
    if(lowRange < highRange){
        ansRange[0] = lowRange;
        ansRange[1] = highRange;
    }
    else{
        ansRange[0] = highRange;
        ansRange[1] = lowRange;        
    }
  }

  public double[] getAnswer(){
    return ansRange;
  }

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     * @throws NumberFormatException    String cannot be parsed into a double
     */
    @Override
  public double checkAnswer(String studentAns[]) throws NumberFormatException{
    try{
        double numStudentAns = Double.parseDouble(studentAns[0]);
        if(numStudentAns >= ansRange[0] && numStudentAns <= ansRange[1])
            return 1.0*gradeVal;
    }
    catch (NumberFormatException nfe){
        System.out.println("error, not a valid number");
    }
    return 0.0;
  }
}
