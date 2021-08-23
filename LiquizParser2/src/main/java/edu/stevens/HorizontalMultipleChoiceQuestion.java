package edu.stevens;

import java.util.ArrayList;

public class HorizontalMultipleChoiceQuestion extends MultipleChoiceQuestion {

    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div class='horizontal'>\n");
        String type = multiAns ? "checkbox" : "radio";
        for (int i = 0; i < choices.length; i++) {
            b.append("<label><input ")
                    .append("type='").append(type)
                    .append("' name='").append(getQuestionId(isQuestion))
                    .append("' value='").append(choices[i]).append("'>")
                    .append(choices[i]).append("</label>\n"); //TODO: make this decent format! displays all answers
        }
        b.append("</div>\n");
    }

    public HorizontalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, String[] choices, ArrayList<String> answers, boolean multiAns) {
        super(questionNumber, partNumber, points, choices, answers, multiAns);
    }

    public HorizontalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, String[] choices, ArrayList<String> answers) {
        super(questionNumber, partNumber, points, choices, answers, false);
    }
}
