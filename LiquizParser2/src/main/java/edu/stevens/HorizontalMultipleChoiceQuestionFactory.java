package edu.stevens;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class HorizontalMultipleChoiceQuestionFactory extends MultipleChoiceQuestionFactory{
    @Override
    public Question makeQuestion(DovParser p, Matcher m) {
        parseQuestion(p, m);
        return new HorizontalMultipleChoiceQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, choices, answers); //TODO:Fix Points
    }
}
