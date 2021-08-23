package edu.stevens;

import java.util.regex.Matcher;

public abstract class QuestionFactory {
    public abstract Question makeQuestion(DovParser p, Matcher m);
}
