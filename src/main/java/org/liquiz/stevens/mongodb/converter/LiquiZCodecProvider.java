/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb.converter;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import questions.Question;
import quiz.Quiz;
import quiz.QuizSubmission;

public class LiquiZCodecProvider implements CodecProvider {

    /**
     *
     * @param <T>
     * @param clazz
     * @param registry
     * @return
     */
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Question.class) {
            return (Codec<T>) new QuestionCodec();
        }
        else if(clazz == Quiz.class) {
            return (Codec<T>) new QuizCodec();
        }
        else if(clazz == QuizSubmission.class) {
            return (Codec<T>) new QuizSubmissionCodec();
        }
        return null;
    }

}