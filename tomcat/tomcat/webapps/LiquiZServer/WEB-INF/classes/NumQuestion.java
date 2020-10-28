//package classes.numquestion;
import java.io.*;
import java.util.ArrayList;
//import classes.question.*;

public class NumQuestion extends Question {
  Double[] ansRange = new Double[2];

  public NumQuestion(Double lowRange, Double highRange, Double gradeVal){
    super(gradeVal);
    this.ansRange[0] = lowRange;
    this.ansRange[1] = highRange;
  }

  public Double[] getAnswer(){
    return ansRange;
  }

  public Double checkAnswer(String studentAns[]){
    Double numStudentAns = Double.parseDouble(studentAns[0]);
    if(numStudentAns >= ansRange[0] && numStudentAns <= ansRange[1])
      return 1.0*gradeVal;
    return 0.0;
  }
}
