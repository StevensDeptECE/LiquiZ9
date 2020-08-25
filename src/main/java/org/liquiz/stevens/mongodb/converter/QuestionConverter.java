/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.mongodb.converter;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import org.bson.Document;
import org.liquiz.stevens.questions.Question;
import org.liquiz.stevens.questions.NumQuestion;
import org.liquiz.stevens.questions.SimpleQuestion;
import org.liquiz.stevens.questions.MultiAnsQuestion;

/**
 *
 * @author ejone
 */
public class QuestionConverter {
    
    /**
     *
     * @param q Question to be converted to document
     * @return Document that has been converted
     */
    public static Document convert(Question q) {
                
		Document doc = new Document("_id", q.getId())
                                .append("gradeVal", q.getGradeValue())
                                .append("name", q.getName());
                doc = q.getDocument(doc);
		return doc;
	}

    /**
     *
     * @param doc Document to convert to Question
     * @return Question that has been converted
     */
    public static Question convert(Document doc) {
                    String type;
                    type = (String)doc.get("name");
                    switch(type.charAt(0)){
                        case 'q':
                        case 'Q':
                        case 's': 
                        case 'S':
                          return new SimpleQuestion(doc.getString("ans"), doc.getDouble("gradeVal"), doc.getString("name"), doc.getObjectId("_id"));
                        case 'N':
                        case 'n':
                          return new NumQuestion(doc.getDouble("low"), doc.getDouble("high"), doc.getDouble("gradeVal"), doc.getString("name"), doc.getObjectId("_id"));
                        case 'M':
                        case 'm':
                          List<Document> ansList = (List<Document>) doc.get("answers");
                          String[] ansArr = new String[ansList.size()];
                          int index = 0;
                          for(Document docAns : ansList){
                            ansArr[index] = docAns.getString("ans");
                            index++;
                          }
                          return new MultiAnsQuestion(ansArr, doc.getDouble("gradeVal"), doc.getBoolean("subWrongAns") , doc.getString("name"), doc.getObjectId("_id"));
                        default:
                          System.out.println("error adding question");     
                    }
            return null;
                    
	}
    
}
