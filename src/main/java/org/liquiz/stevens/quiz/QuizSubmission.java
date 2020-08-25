/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.quiz;
/**
 *
 * @author ejone
 */
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import org.liquiz.stevens.questions.Question;

public class QuizSubmission{
  TreeMap<String, String[]> userAnswers;
  double[] questionGradesArr;
  double grade;
  long quizId;
  String userId;
  String fullName;
  Date dateSubmitted;
  private ObjectId mongoId;

    /**
     * For initializing a new quiz
     * @param quizId the id of the quiz that the answers are received from
     * @param userId the id of the user from canvas
     * @param inputsMap a treemap of the users inputs for the quiz
     * @param q the quiz which this submission will be graded based off of so 
     *          update grade can be called in this instructor
     */
    public QuizSubmission(long quizId, String userId, String fullName, TreeMap<String,String[]> inputsMap, Quiz q){
    this.quizId = quizId;
    this.userId = userId;
    this.grade = 0.0;
    this.userAnswers = inputsMap;
    this.fullName = fullName;
    dateSubmitted = new Date();
    questionGradesArr = new double[userAnswers.size()];
    updateGrade(q.getQuestionsMap());
  }
  
    /**
     * For initializing a quiz from mongo database
     * @param mongoId the mongoId associated with the quiz
     * @param quizId the id of the quiz that the answers are received from
     * @param userId the id of the user from canvas
     * @param grade the grade that was previously calculated for this quiz
     * @param questionGradesArr The array of grades corresponding to each question
     * @param dateSubmitted Date that the quizSubmission was created/finished
     */
    public QuizSubmission(ObjectId mongoId, long quizId, String userId, String fullName, double grade, double[] questionGradesArr, Date dateSubmitted){
      this.mongoId = mongoId;
      this.quizId = quizId;
      this.userId = userId;
      this.grade = grade;
      this.fullName = fullName;
      this.dateSubmitted = dateSubmitted;
      this.questionGradesArr = questionGradesArr;
      userAnswers = new TreeMap<>();
  }
  
    /**
     *
     * @return mongoId of the quiz
     */
    public final ObjectId getId(){
      return mongoId;
  }
  
    /**
     *
     * @param newId the new id that mongoId will be set to
     */
    public final void setId(ObjectId newId){
      mongoId = newId;
  }
  
    /**
     *
     * @return QuizId of this submission
     */
    public final long getQuizId() {
      return quizId;
  }
  
    /**
     *
     * @param newID the new name of quizId for this submission
     */
    public final void setQuizId(long newID) {
      quizId = newID;
  }
   
    /**
     *
     * @return the id of the user who is associated with this submission
     */
    public final String getUserId() {
      return userId;
    }

    public final String getFullName() {return fullName;}

    public final void setFullName(String newName) {fullName = newName;}
    /**
     *
     * @param newUserId the new user id associated with this submission
     */
    public final void setUserId(String newUserId) {
      userId = newUserId;
    }
   
    /**
     *
     * @return this of grades for each question in an array
     */
    public final double[] getQuestionGrades() {
      return questionGradesArr;
    }
  
    /**
     *
     * @param index the index of the question grade to be returned
     * @return the grade of the specified question
     */
    public final double getQuestionGrade(int index) {
      return questionGradesArr[index];
    }
  
    /**
     *
     * @param index the index of the question grade to be changed
     * @param newGrade the new grade for the question in the submission
     */
    public final void setQuestionGrade(int index, double newGrade) {
      questionGradesArr[index] = newGrade;
    }
  
    /**
     *
     * @return the overall grade of the submission
     */
    public double getGrade() {
        return grade;
    }
    
    public void setGrade(double newGrade) {
        grade = newGrade;
    }

    /**
     *
     * @return the treemap of all of the inputs
     */
    public TreeMap<String, String[]> getInputs() {
      return userAnswers;
  }
    
    public final void setDateSubmitted(Date date) {
        dateSubmitted = date;
    }
    
    public final Date getDateSubmitted() {
        return dateSubmitted;
    }
  
    /**
     *
     * @param key the id of the question that is being added
     * @param userAnswer the student's input associated with the new question
     */
    public final void addAnswer(String key, String[] userAnswer) {
      userAnswers.put(key, userAnswer);
  }

    /**
     * Updates the grade for this submission based on the question map passed 
     * @param questionsMap map of the questions that are used to grade 
     */
    public void updateGrade(TreeMap<String, Question> questionsMap){
      int index = 0;
      for(Map.Entry<String, Question> entry : questionsMap.entrySet()){
        Question q = entry.getValue();


        String[] inputsString = userAnswers.get(q.getName());

        double qGrade;
        if(q==null)
           System.out.println("error: could not find question in quiz");
        else if(inputsString != null){
          qGrade = q.checkAnswer(inputsString);
          grade+= qGrade;
          questionGradesArr[index++] = qGrade;
        }
      }
  }
  
}