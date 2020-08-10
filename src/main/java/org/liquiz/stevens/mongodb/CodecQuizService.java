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
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
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
public class CodecQuizService {

    @Autowired
    private MongoClient mongoClient;

        
    /**
     *
     * @return List of Quizes in the collection
     */
    public List<Quiz> list(){
        List<Quiz> list = new ArrayList<>();
        try (MongoCursor<Quiz> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }
        
    /**
     *
     * @param quiz Quiz that is to be added to the collection
     */
    public void add(Quiz quiz){
        getCollection().insertOne(quiz);
    }
    
    /**
     *
     * @param quiz Quiz that is to be deleted from the collection
     */
    public void delete(Quiz quiz) {
        try {
            getCollection().deleteOne(new Document( "_id", quiz.getId() ));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean replaceQuiz(Quiz quiz) {
        try {
            getCollection().findOneAndReplace(new Document("quizName", quiz.getQuizName()), quiz);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    /**
     * Used to get one Quiz from database with search parameters
     * @param doc Document with elements to search for
     * @return Quiz that matches elements
     */
    public Quiz getOne(Document doc){
        FindIterable<Quiz> iterable = getCollection().find(doc).limit(1);
        Iterator iterator = iterable.iterator();
        Quiz quiz = null;
        if(iterator.hasNext())
            quiz =(Quiz) iterator.next();
        return quiz;
    }

    public Quiz getOne(long id) {
        FindIterable<Quiz> iterable = getCollection().find(eq("quizId", id)).limit(1);
        Iterator iterator = iterable.iterator();
        Quiz quiz = null;
        if(iterator.hasNext())
            quiz =(Quiz) iterator.next();
        return quiz;
    }
    
    /**
     * Returns a list of every Quiz that matches the search arguments.
     * Can be used to find every quiz for a class.
     * @param doc Document with elements to search for
     * @return List of Quizzes matching search arguments
     */
    public ArrayList<Quiz> getList(Document doc) {
        ArrayList<Quiz> submissionList = new ArrayList();
        try (MongoCursor<Quiz> cursor = getCollection().find(doc).iterator()) {
         while (cursor.hasNext()) {
                submissionList.add(cursor.next());
            }
        }
        return submissionList;
    }
    
    /**
     *
     * @param quizId String id of the quiz to search for
     * @return boolean on whether it exists in the collection
     */
    public boolean exists(long quizId){
        return getCollection().find(eq("quizId", quizId)).first()!=null;
    }

    public boolean exists(Document doc){
        return getCollection().find(doc).first()!=null;
    }
        
    private MongoCollection<Quiz> getCollection(){
        return mongoClient.getDatabase("quiz").getCollection("quiz", Quiz.class);
    }
}
