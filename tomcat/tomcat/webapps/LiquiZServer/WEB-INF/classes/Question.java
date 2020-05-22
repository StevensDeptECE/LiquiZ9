//package classes.question;
import java.io.*;
import java.util.ArrayList;

public abstract class Question {
  Double gradeVal;

  @Override
  public final String type(){
    return getClass().getSimpleName();
  }

  public Question(String type, Double gradeVal){
    this.gradeVal = gradeVal;
  }

  public Double getGradeValue(){
    return gradeVal;
  }

  public abstract Double checkAnswer(String studentAns[]);

}
