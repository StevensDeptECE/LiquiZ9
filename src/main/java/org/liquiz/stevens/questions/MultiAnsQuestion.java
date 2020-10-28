/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import org.bson.Document;
import org.bson.types.ObjectId;
/**
 *
 * @author ejone
 */
public class MultiAnsQuestion extends Question {
  boolean subWrongAns;
  private String[] answers;

    /**
     * For initializing a new MultiAnsQuestion
     * @param ans String array for the answers
     * @param gradeVal double for the grade value
     * @param subWrongAns boolean for setting whether to subtract for wrong answers
     * @param questionsMap TreeMap that question is added to
     * @param name String for the name 
     */
    public MultiAnsQuestion(String ans[], double gradeVal, boolean subWrongAns, TreeMap<String, Question> questionsMap, String name) {
    super(gradeVal, questionsMap, name);
    this.subWrongAns = subWrongAns;
    answers = ans.clone();
  }
  
    /**
     * For initializing a MultiAnsQuestion from the database
     * @param ans String array for the answers
     * @param gradeVal double for the grade value
     * @param subWrongAns boolean for setting whether to subtract for wrong answers
     * @param name String for the name
     * @param id ObjectId for the id of the question
     */
    public MultiAnsQuestion(String ans[], double gradeVal, boolean subWrongAns, String name, ObjectId id) {
    super(gradeVal, name, id);
    this.subWrongAns = subWrongAns;
    answers = ans.clone();
  }

    /**
     *
     * @return subWrongAns boolean that tells whether to subtract if an answer is not selected
     */

  public final boolean getSubWrongAns() {
    return subWrongAns;
  }
  
    /**
     *
     * @return answers in array format
     */
    public String[] getAnswers() {
    return answers;
  }
  
    /**
     *
     * @return array of answers in string format
     */
    @Override
  public String getAnswer() {
      return Arrays.toString(answers);
  }
  
    /**
     *
     * @param doc the document to append the values of multiansquestion
     * @return the doc with the appended values
     */
    @Override
  public Document getDocument(Document doc){
      List<Document> docAnsList = new ArrayList<>();
      for(String a : answers){
          Document docAns = new Document("ans", a);
          docAnsList.add(docAns);
      }
      doc.append("answers", docAnsList)
             .append("subWrongAns", subWrongAns);
      return doc;
  }
  
//TODO: add posibility to subtract points if one submitted is wrong 

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     */
  @Override
  public double checkAnswer(String studentAns[]) {
    double finishedGradeVal = 0.0;
    final double pointsPerAnswer = gradeVal/answers.length;
    for(String answer : answers){
      int index = 0;
      for(String a : studentAns) {
        a = a.trim();
        index++;
        if(answer.equals(a)){
          finishedGradeVal += pointsPerAnswer;
          break;
        }
        else if(index == studentAns.length && subWrongAns)
          finishedGradeVal -= pointsPerAnswer;
      }
    }
    finishedGradeVal = Math.round(finishedGradeVal*1000)/1000;
    return (finishedGradeVal > 0.0) ? finishedGradeVal : 0.0;
  }
}
