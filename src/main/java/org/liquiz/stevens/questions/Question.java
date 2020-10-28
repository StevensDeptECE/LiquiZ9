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
public abstract class Question {
  double gradeVal;
  String name;
  private ObjectId id;

    /**
     * For initializing a new question
     * @param gradeVal double for the grade value
     * @param questionsMap TreeMap for all of the questions which it should be added to
     * @param name String for the name
     */
    public Question(double gradeVal, TreeMap<String, Question> questionsMap, String name) {
    this.gradeVal = gradeVal;
    this.name = name;
    this.id = new ObjectId();
    questionsMap.put(name, this);
  }
  
    /**
     * For initializing a Question from the database
     * @param gradeVal double for the grade value
     * @param name String for the name
     * @param id ObjectId for the id
     */
    public Question(double gradeVal, String name, ObjectId id) {
    this.id = id;
    this.gradeVal = gradeVal;
    this.name = name;
  }
  
    /**
     *
     * @return ObjectId of the question
     */
    public final ObjectId getId(){
      return id;
  }
  
    /**
     *
     * @param newId the new ObjectId of the question for the database
     */
    public final void setId(ObjectId newId){
      id = newId;
    }

    /**
     *
     * @return String holding the class type
     */
    public final String type() {
        return getClass().getSimpleName();
    }
  
    /**
     *
     * @return String holding the name of the question
     */
    public final String getName() {
      return name;
  }

    /**
     *
     * @return double representing the grade value of the question
     */
    public final double getGradeValue() {
        return gradeVal;
    }
  
    /**
     *
     * @return String of the answer for the question
     */
    public abstract String getAnswer();
  
    /**
     *
     * @param studentAns the inputs the student submitted for the question
     * @return double representing the grade value based on the question's answers
     */
    public abstract double checkAnswer(String studentAns[]);
  
    /**
     *
     * @return String representing html for question
     */
  
    /**
     *
     * @param doc Document to be appended to based on question type
     * @return The same document with appended values
     */
    public abstract Document getDocument(Document doc);

}
