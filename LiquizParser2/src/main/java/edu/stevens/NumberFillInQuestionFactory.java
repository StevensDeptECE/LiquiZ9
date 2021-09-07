package edu.stevens;

import java.util.regex.Matcher;

public class NumberFillInQuestionFactory extends QuestionFactory{
    @Override
    public Question makeQuestion(LiquiZParser p, Matcher m) {
        double answer = Double.parseDouble(m.group(3));
        return new NumberFillinQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, answer); //TODO:Fix Points
    }
}
