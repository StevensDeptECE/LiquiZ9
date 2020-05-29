/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

public class SimpleQuestion extends Question{
  String ans;
  boolean caseSens;
  boolean spaceSens;

  public SimpleQuestion(String ans, double gradeVal, String qType){
    super(gradeVal);
    char type = qType.charAt(0);
    caseSens = true;
    if(type == 'Q' || type == 'S')
        caseSens = false;
    spaceSens = true;
    if(type == 's' || type == 'S')
        spaceSens = false;
    this.ans = ans;
    if(!caseSens)
      this.ans = this.ans.toLowerCase();
    if(!spaceSens)
      this.ans = this.ans.replaceAll("\\s", "");
  }

  public String getAnswer(){
    return ans;
  }

  public boolean getCaseSensitivity(){
    return caseSens;
  }

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
    @Override
  public double checkAnswer(String studentAns[]){
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
