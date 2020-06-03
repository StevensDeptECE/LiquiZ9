/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.HashMap;

public class SimpleQuestion extends Question {
  String ans;
  boolean caseSens;
  boolean spaceSens;

  //TODO: set number space sensitivity
  public SimpleQuestion(String ans, double gradeVal, String qType, HashMap<String, Question> questionsMap) {
    super(gradeVal, questionsMap, qType);
    char type = qType.charAt(0);
    caseSens = !(type == 'Q' || type == 'S');
    spaceSens = !(type == 's' || type == 'S');
    this.ans = ans;
    if(!caseSens)
      this.ans = this.ans.toLowerCase();
    if(!spaceSens)
      this.ans = this.ans.replaceAll("\\s", "");
  }

  public String getAnswer() {
    return ans;
  }

  public boolean getCaseSensitivity() {
    return caseSens;
  }
  
  public boolean getSpaceSensitivity() {
      return spaceSens;
  }

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
  @Override
  public double checkAnswer(String studentAns[]) {
    String caseStudentAns = studentAns[0];
    if(!caseSens)
      caseStudentAns = caseStudentAns.toLowerCase();
    if(!spaceSens)
      caseStudentAns = caseStudentAns.replaceAll("\\s", "");
    if(caseStudentAns.equals(ans))
      return 1.0*gradeVal;
    return 0.0;
  }

}
