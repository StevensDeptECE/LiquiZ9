package edu.stevens;

import java.util.ArrayList;

public class DropDownQuestion extends MultipleChoiceQuestion {
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div><select>\n");
        for (int i = 0; i < choices.length; i++) {
            b.append("<option value='").append(choices[i]).append("'>").append(choices[i]).append("</option>\n"); //TODO: make this decent format! displays all answers
        }
        b.append("<select></div>\n");
    }
//    public void buildAnswers(StringBuilder b) {
//        b.append("getPoints()").append("\t").append(answers.set(0));//TODO: figure out how to get points
//        for(int i = 1; i < answers.length; i++) {
//            b.append(",").append(answers.set(i));
//        }
//        b.append("\n");
//    }
    public String getqIdType() {
        return "q_";
    }
    public DropDownQuestion(int questionNumber, int partNumber, double points, boolean multiAns, String[] choices, ArrayList<String> answers) {
        super(questionNumber, partNumber, points, choices, answers ,multiAns);
    }
    public DropDownQuestion(int questionNumber, int partNumber, double points, String[] choices, ArrayList<String> answers) {
        super(questionNumber, partNumber, points, choices, answers, false);
    }
}