/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.liquiz.stevens.questions.Question;
import org.liquiz.stevens.quiz.Quiz;
import org.liquiz.stevens.quiz.QuizSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ejone
 */
@Repository
@ApplicationScoped
public class CodecQuizSubmissionService {

    @Autowired
    private MongoClient mongoClient;

        
    /**
     *
     * @return List of QuizSubmissions in the collection
     */
    public List<QuizSubmission> list(){
        List<QuizSubmission> list = new ArrayList<>();
        try (MongoCursor<QuizSubmission> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }
        
    /**
     * Add a quizSubmission to the database
     * @param quiz QuizSubmission that is to be added to the collection
     */
    public void add(QuizSubmission quiz){
        getCollection().insertOne(quiz);
    }
    
    /**
     * Delete a quizSubmission from the database
     * @param quiz QuizSubmission that is to be deleted to the collection
     */
    public void delete(QuizSubmission quiz) {
        try {
            getCollection().deleteOne(new Document( "_id", quiz.getId() ));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean delete(String submissionId) {
        try{
            ObjectId id = new ObjectId(submissionId);
            getCollection().deleteOne(new Document("_id", id));
            return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    /**
     * Used to get one quizSubmission from database with search parameters
     * @param doc Document with elements to search for
     * @return QuizSubmission that matches elements
     */
    public QuizSubmission getOne(Document doc) {
        FindIterable<QuizSubmission> iterable = getCollection().find(doc).limit(1);
        Iterator iterator = iterable.iterator();
        QuizSubmission quizSubmission = null;
        if(iterator.hasNext())
            quizSubmission =(QuizSubmission) iterator.next();
        return quizSubmission;
    }
    
    /**
     * Returns a list of every quizSubmission that matches the search arguments
     * @param doc Document with elements to search for
     * @return List of quizSubmission matching search arguments
     */
    public ArrayList<QuizSubmission> getList(Document doc) {
        ArrayList<QuizSubmission> submissionList = new ArrayList();
        try (MongoCursor<QuizSubmission> cursor = getCollection().find(doc).iterator()) {
         while (cursor.hasNext()) {
                submissionList.add(cursor.next());
            }
        }
        return submissionList;
    }
    
    /**
     * Returns an array of doubles to see the averages scores for each question
     * @param quizId id value of the quiz to search for
     * @return the average scores for each question
     */
    public double[] getAvgQuizScores(String quizId) {
        ArrayList<QuizSubmission> quizList;
        quizList = getList(new Document("quizId", quizId));
        int numOfSubmissions = quizList.size();
        double[] avgQuestionScores = new double[quizList.get(0).getQuestionGrades().length];
        Arrays.fill(avgQuestionScores, 0.0);
        for(QuizSubmission quizSub : quizList){
            double[] scores = quizSub.getQuestionGrades();
            for(int i = 0; i < scores.length; i++){
                double scoreToAdd = Math.round((scores[i]/numOfSubmissions)/1000)*1000;
                avgQuestionScores[i] += scoreToAdd;
            }
        }
        return avgQuestionScores;
    }

    public double getAvgQuizScore(long quizId) {
        ArrayList<QuizSubmission> quizList;
        quizList = getList(new Document("quizId", quizId));
        double numOfSubmissions = quizList.size();
        double avgGrade = 0.0;
        for(QuizSubmission qSub : quizList) {
            avgGrade += qSub.getGrade();
        }

        double ret = Math.round(avgGrade/numOfSubmissions*1000.0)/1000.0;
        return ret;
    }

    /**
     * Returns a double that represents the highest grade the student received for the given quiz
     * @param quizId id of quiz to search for
     * @param userId id of user to search for
     * @return The highest grade among the users attempts at the quiz
     */
    public double getHighestGrade(String quizId, String userId){
        Document doc = new Document("quizId", quizId)
                                   .append("userId", userId);
        double grade = 0.0;
        FindIterable<QuizSubmission> iterable = getCollection().find(doc);
        Iterator iterator = iterable.iterator();
        QuizSubmission quizSub;
        while(iterator.hasNext()){
            quizSub = (QuizSubmission) iterator.next();
            if(quizSub.getGrade() > grade)
                grade = quizSub.getGrade();
        }
        return grade;
    }
    
    /**
     * Returns the number of submissions for the given arguments
     * The document must have "quizId" and "userId" appended to it to find the 
     * number of submissions for one student. 
     * @param doc Document with elements to search for
     * @return number of submissions the student has completed for a quiz
     */
    public int getTries(Document doc) {
        FindIterable<QuizSubmission> iterable = getCollection().find(doc);
        Iterator iterator = iterable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
    
    /**
     * Updates the grades of quiz submissions if the quiz is updated accordingly
     * @param quiz the quiz that has new details
     */
    public void updateGrades(Quiz quiz) {
        ArrayList<QuizSubmission> submissionList = (ArrayList<QuizSubmission>) getCollection().find(new Document("quizId", quiz.getQuizId()));
        for(QuizSubmission quizSub : submissionList){
            quizSub.updateGrade(quiz.getQuestionsMap());
            getCollection().findOneAndReplace(new Document("mongoId", quizSub.getId()), quizSub);
        }
    }
    
    /**
     * Updates one question grade on a given quiz
     * @param q question to update
     * @param doc holds parameters of search
     */
    public void updateOneSubmissionGrade(Question q, Document doc) {
        int index = Integer.parseInt(q.getName().split("_")[2]);
        QuizSubmission quizSub = getOne(doc);
        quizSub.setQuestionGrade(index-1, q.getGradeValue());
        double newGrade = quizSub.getGrade() + q.getGradeValue();
        quizSub.setGrade(newGrade);
        getCollection().findOneAndReplace(new Document("mongoId", quizSub.getId()), quizSub);
    }
    
    /**
     *
     * @param quizId id of the quiz to search for
     * @return boolean boolean on whether it exists in the collection
     */
    public boolean exists(String quizId){
        return getCollection().find(eq("quizId", quizId)).first()!=null;
    }
        
    private MongoCollection<QuizSubmission> getCollection(){
        return mongoClient.getDatabase("quiz").getCollection("quizSubmission", QuizSubmission.class);
    }
}
