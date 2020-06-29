/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questions;

import java.util.TreeMap;
import org.bson.Document;
import org.bson.types.ObjectId;

public class SimpleQuestion extends Question {
  String ans;
  boolean caseSens;
  boolean spaceSens;

  //TODO: set number space sensitivity

    /**
     * For initializing a new SimpleQuestion
     * @param ans String for the answer
     * @param gradeVal double for the grade value
     * @param qType String for the question type
     * @param questionsMap TreeMap question is added to
     */
  public SimpleQuestion(String ans, double gradeVal, String qType, TreeMap<Integer, Question> questionsMap) {
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

    /**
     * For initializing a SimpleQuestion from the database
     * @param ans String for the answer 
     * @param gradeVal double for the grade value
     * @param qType String for the question type
     * @param id ObjectId for the id of the question
     */
    public SimpleQuestion(String ans, double gradeVal, String qType, ObjectId id) {
    super(gradeVal, qType, id);
    char type = qType.charAt(0);
    caseSens = !(type == 'Q' || type == 'S');
    spaceSens = !(type == 's' || type == 'S');
    this.ans = ans;
    if(!caseSens)
      this.ans = this.ans.toLowerCase();
    if(!spaceSens)
      this.ans = this.ans.replaceAll("\\s", "");
  }

    /**
     *
     * @return boolean holding with the question is case sensitive
     */
    public boolean getCaseSensitivity() {
        return caseSens;
    }
  
    /**
     *
     * @return boolean holding whether the question is space sensitive
     */
    public boolean getSpaceSensitivity() {
      return spaceSens;
    }
  
    /**
     *
     * @return String of the answer for the question
     */
    @Override
    public String getAnswer() {
        return ans;
    }
  
    /**
     *
     * @param doc Document to be appended to hold the simple question values
     * @return The same document with appended values
     */
    @Override
    public Document getDocument(Document doc) {
      doc.append("ans", ans)
             .append("caseSens", caseSens)
             .append("spaceSens", spaceSens);
      return doc;
    }
  

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
  @Override
  public double checkAnswer(String studentAns[]) {
    String caseStudentAns = studentAns[0].trim();
    double newGrade = 0.0;
    if(!caseSens)
      caseStudentAns = caseStudentAns.toLowerCase();
    if(!spaceSens)
      caseStudentAns = caseStudentAns.replaceAll("\\s", "");
    if(caseStudentAns.equals(ans)){
      newGrade = 1.0*gradeVal;
      setGrade(newGrade);
      return newGrade;
    }
    setGrade(newGrade);
    return newGrade;
  }

}
