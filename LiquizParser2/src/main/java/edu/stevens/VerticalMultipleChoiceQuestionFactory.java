package edu.stevens;

import java.util.regex.Matcher;

public class VerticalMultipleChoiceQuestionFactory extends MultipleChoiceQuestionFactory{
    @Override
    public Question makeQuestion(DovParser p, Matcher m) {
        parseQuestion(p, m);
        return new VerticalMultipleChoiceQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, choices, answers); //TODO:Fix Points
    }
}
