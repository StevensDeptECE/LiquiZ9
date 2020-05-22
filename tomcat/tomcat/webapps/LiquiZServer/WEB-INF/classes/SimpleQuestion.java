import java.io.*;
import java.util.ArrayList;

public class SimpleQuestion extends Question{
  String ans;
  Boolean caseSens;

  public SimpleQuestion(String ans, Double gradeVal, Boolean caseSens){
    super(gradeVal);
    this.caseSens = caseSens;
    if(caseSens)
      this.ans = ans;
    else
      this.ans = ans.toLowerCase();
  }

  public String getAnswer(){
    return ans;
  }

  public Boolean getCaseSensitivity(){
    return caseSens;
  }

  public Double checkAnswer(String studentAns[]){
    String caseStudentAns = studentAns[0];
    if(!caseSens)
      caseStudentAns = caseStudentAns.toLowerCase();
    if(caseStudentAns.equals(ans))
      return 1.0*gradeVal;
    return 0.0;
  }

}
