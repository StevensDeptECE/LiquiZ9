package edu.stevens;

import java.util.regex.Matcher;

public class FillInQuestionFactory extends QuestionFactory{
    @Override
    public Question makeQuestion(LiquiZParser p, Matcher m) {
        String answer = m.group(3);
        return new FillinQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, answer); //TODO:Fix Points
    }
}
