//package classes.question;
import java.io.*;
import java.util.ArrayList;

public abstract class Question {
  Double gradeVal;

  public Question( Double gradeVal){
    this.gradeVal = gradeVal;
  }

  public final String type(){
    return getClass().getSimpleName();
  }

  public Double getGradeValue(){
    return gradeVal;
  }

  public abstract Double checkAnswer(String studentAns[]);

}
