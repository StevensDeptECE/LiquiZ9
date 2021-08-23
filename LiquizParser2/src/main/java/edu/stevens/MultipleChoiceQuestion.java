package edu.stevens;

import java.util.ArrayList;

public abstract class MultipleChoiceQuestion extends Question {
    protected String[] choices;
    protected ArrayList<String> answers;
    protected boolean multiAns;
    //TODO: This class has only what all multiplechoice questions have in common

    public void buildAnswers(StringBuilder b) {
        b.append(getPoints()).append("\t").append(answers.get(0));//TODO: figure out how to get points
        for(int i = 1; i < answers.size(); i++) {
                b.append(",").append(answers.get(i));
        }
        b.append("\n");
    }
    public MultipleChoiceQuestion(int questionNumber, int partNumber, double points, String[] choices, ArrayList<String> answers, boolean multiAns) {
        super(questionNumber, partNumber, points);
        this.answers = answers;
        this.multiAns = multiAns;
        this.choices = choices;
    }

}
