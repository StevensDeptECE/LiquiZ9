package question;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Enumeration;
import java.util.ArrayList;

public class Question {
  String type;
  String ans;
  Double gradeVal;
  Boolean caseSens;

  public Question(String ans, Double gradeVal, Boolean case){
    this.type = "Question";
    this.ans = ans.toLowerCase();;
    this.gradeVal = gradeVal;
    this.caseSens = case;
  }

  public String getType(){
    return type;
  }

  public double getGradeValue(){
    return gradeVal;
  }

  public String getAnswer(){
    return ans;
  }

  public Boolean getCaseSensitivity(){
    return caseSens;
  }

  public Double checkAnswer(String studentAns){
    lCaseStudentAns = studentAns.toLowerCase();
    if(lCaseStudentAns.compare(ans)==0)
      return 1.0*gradeVal;
    return 0.0;
  }

}
