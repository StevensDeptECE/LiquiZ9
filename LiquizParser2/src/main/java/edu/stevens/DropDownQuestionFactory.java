package edu.stevens;

import java.util.regex.Matcher;

public class DropDownQuestionFactory extends MultipleChoiceQuestionFactory{
    @Override
    public Question makeQuestion(LiquiZParser p, Matcher m) {
        parseQuestion(p, m);
        return new DropDownQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, choices, answers); //TODO:Fix Points
    }
}
