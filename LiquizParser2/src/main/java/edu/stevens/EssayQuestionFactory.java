package edu.stevens;

import java.util.regex.Matcher;

public class EssayQuestionFactory extends QuestionFactory{
    @Override
    public Question makeQuestion(DovParser p, Matcher m) {
        String defaultText = "Please input answer here";
        String answer = m.group(3);
        return new EssayQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, defaultText, answer); //TODO:Fix Points
    }
}

