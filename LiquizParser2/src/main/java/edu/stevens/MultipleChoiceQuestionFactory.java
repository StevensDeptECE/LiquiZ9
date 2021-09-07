package edu.stevens;

import java.util.ArrayList;
import java.util.regex.Matcher;

public abstract class MultipleChoiceQuestionFactory extends QuestionFactory{
    protected String[] choices;
    protected ArrayList<String> answers;
    protected void parseQuestion(LiquiZParser p, Matcher m) {
        String choiceString = m.group(3); //$mch:answer1,answer2,answer3$
        choices = choiceString.split(",");
        ArrayList<String> answers = new ArrayList<>();
        for (int i=0; i<choices.length; i++) {
            if (choices[i].startsWith("*")) {
                choices[i] = choices[i].substring(1);
                answers.add(choices[i]);
            }
        }
        this.answers = answers;
    }
}
