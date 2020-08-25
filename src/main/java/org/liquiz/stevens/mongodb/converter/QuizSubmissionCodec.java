/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.mongodb.converter;

import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.liquiz.stevens.quiz.QuizSubmission;

/**
 * Mongo decoder for Orders.
 */
public class QuizSubmissionCodec implements CollectibleCodec<QuizSubmission> {

    private final CodecRegistry registry;
    private final Codec<Document> documentCodec;
    private final QuestionConverter QuestionConverter;

    /**
     * Default constructor.
     */
    public QuizSubmissionCodec() {
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.documentCodec = this.registry.get(Document.class);
        this.QuestionConverter = new QuestionConverter();
    }

    /**
     * Codec constructor.
     * @param codec The existing codec to use.
     */
    public QuizSubmissionCodec(Codec<Document> codec) {
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.documentCodec = codec;
        this.QuestionConverter = new QuestionConverter();
    }

    /**
     * Registry constructor.
     * @param registry The CodecRegistry to use.
     */
    public QuizSubmissionCodec(CodecRegistry registry) {
        this.registry = registry;
        this.documentCodec = this.registry.get(Document.class);
        this.QuestionConverter = new QuestionConverter();
    }

    /**
     * Encode the passed Order into a Mongo/BSON document.
     * @param writer The writer to use for encoding.
     * @param quiz The Quiz to encode.
     * @param encoderContext The EncoderContext to use for encoding.
     */
    @Override
    public void encode(
                    BsonWriter writer,
                    QuizSubmission quiz,
                    EncoderContext encoderContext
                ) {
        List<Document> questionDocumentList = new ArrayList<>();
        for(Map.Entry<String, String[]> entry : quiz.getInputs().entrySet()){
            String[] arr = entry.getValue();
            List<Document> userAnswers = new ArrayList<>();
            for(String a : arr){
                Document doc = new Document("ans", a);
                userAnswers.add(doc);
            }
            Document doc = new Document("questionId", entry.getKey())
                                .append("userAnswerArr", userAnswers);
            questionDocumentList.add(doc);
        }
        List<Document> questionGradesList = new ArrayList<>();
        for(double g : quiz.getQuestionGrades()){
            Document doc = new Document("qgrade", g);
            questionGradesList.add(doc);
        }
            
        Document document = new Document("_id", quiz.getId())
                .append("quizId", quiz.getQuizId())
                .append("userId", quiz.getUserId())
                .append("fullName", quiz.getFullName())
                .append("grade", quiz.getGrade())
                .append("dateSubmitted", quiz.getDateSubmitted())
                .append("userAnswers", questionDocumentList)
                .append("questionGradesArr", questionGradesList);


        documentCodec.encode(writer, document, encoderContext);
    }

    /**
     * Get the class that this Codec works with.
     * @return Returns the class that this Codec works with.
     */
    @Override
    public Class<QuizSubmission> getEncoderClass() {
        return QuizSubmission.class;
    }

    /**
     * Decodes a Mongo/BSON document into an Order.
     * @param reader The reader containing the Document.
     * @param decoderContext The DecoderContext to use for decoding.
     * @return Returns the decoded Order.
     */
    @Override
    public QuizSubmission decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        
        List<Document> questionGradesList = (List<Document>) document.get("questionGradesArr");
        double[] questionGradesArr = new double[questionGradesList.size()];
        int gIndex = 0;
        for(Document doc : questionGradesList){
            questionGradesArr[gIndex++] = doc.getDouble("qgrade");
        }
        
        QuizSubmission quiz = new QuizSubmission(document.getObjectId("_id"), document.getLong("quizId"),
                document.getString("userId"), document.getString("fullName"), document.getDouble("grade"), questionGradesArr, document.getDate("dateSubmitted"));

        List<Document> docArr = (List<Document>) document.get("userAnswers");
        for (Document doc : docArr) {
            List<Document> ansList = (List<Document>) doc.get("userAnswerArr");
            String[] ansArr = new String[ansList.size()];
            int index = 0;
            for(Document docAns : ansList){
                ansArr[index] = docAns.getString("ans");
                index++;
            }
            quiz.addAnswer( doc.getString("questionId"),  ansArr);
        }

        return quiz;
    }

    /**
     * Generates a new ObjectId for the passed Order (if absent).
     * @param quiz The Quiz to work with.
     * @return Returns the passed Order with a new id added if there
     * was none.
     */
    @Override
    public QuizSubmission generateIdIfAbsentFromDocument(QuizSubmission quiz) {
        if (!documentHasId(quiz)) {
            quiz.setId(new ObjectId());
        }

        return quiz;
    }

    /**
     * Returns whether or not the passed Order has an id.
     * @param quiz The Quiz that you want to check for
     * the presence of an id.
     * @return Returns whether or not the passed Order has an id.
     */
    @Override
    public boolean documentHasId(QuizSubmission quiz) {
        return (quiz.getId() != null);
    }

    /**
     * Gets the id of the passed Order.If there is no id, it will
 throw an IllegalStateException (RuntimeException).
     * @param quiz The Quiz whose id you want to get.
     * @return Returns the id of the passed Order as a BsonValue.
     */
    @Override
    public BsonValue getDocumentId(QuizSubmission quiz) {
        if (!documentHasId(quiz)) {
            throw new IllegalStateException("The document does not contain an _id");
        }

        return new BsonString(quiz.getId().toHexString());
    }
}