/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.questions;

import java.util.TreeMap;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author ejone
 */
public class NumQuestion extends Question {
  private double low, high;
  
    /**
     * For initializing a new NumQuestion
     * @param low double for the low value
     * @param high double for the high value
     * @param gradeVal double for the grade value
     * @param questionsMap TreeMap the question is added to
     * @param name String for the name of the question
     */
    public NumQuestion(double low, double high, double gradeVal, TreeMap<String, Question> questionsMap, String name) {
    super(gradeVal, questionsMap, name);
    if(low < high){
        this.low = low;
        this.high = high;
    }
    else{
        this.low = high;
        this.high = low;        
    }
  }
  
    /**
     * For initializing a NumQuestion from the database
     * @param low double for the low value
     * @param high double for the high value
     * @param gradeVal double for the grade value
     * @param name String for the name
     * @param id ObjectId for the id of the question
     */
    public NumQuestion(double low, double high, double gradeVal, String name, ObjectId id) {
    super(gradeVal, name, id);
    if(low < high){
        this.low = low;
        this.high = high;
    }
    else{
        this.low = high;
        this.high = low;        
    }
  }
  
  

  /*public double[] getAnswer() {
    return new double[]{low, high};
  }*/
  
  @Override
  public String getAnswer() {
      return Double.toString(low) + " - " + Double.toString(high);
  }
  
    /**
     *
     * @param doc the document to append the values of multiansquestion
     * @return the doc with the appended values
     */
    @Override
  public Document getDocument(Document doc){
          doc.append("low", low)
             .append("high", high);
      return doc;
  }

    /**
     *
     * @param studentAns    the answers taken from the students page
     * @return double       the value of the question after it has been graded
     * @throws NumberFormatException    String cannot be parsed into a double
     */
    @Override
  public double checkAnswer(String studentAns[]) throws NumberFormatException {
    double newGrade = 0.0;
    try {
        double numStudentAns = Double.parseDouble(studentAns[0]);
        if(numStudentAns >= low && numStudentAns <= high){
            newGrade = 1.0*gradeVal;
            return newGrade;
        }
    }
    //TODO: display on the client something went wrong
    catch (NumberFormatException nfe) {
         System.out.println("not a valid number for question" + name);
    }
    return newGrade;
  }
}
