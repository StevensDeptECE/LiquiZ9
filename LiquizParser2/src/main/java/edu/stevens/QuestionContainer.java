package edu.stevens;

import java.util.*;

public class QuestionContainer {
    private String qcId; // TODO: unique id for the question container, generated into html. Is it ever used?
    private String name;
    private float points;
    private ArrayList<DisplayElement> displayElements;
    private QuestionContainerSpec qcs;

    public QuestionContainer() { displayElements = new ArrayList<>(); name = ""; }
    public QuestionContainer(float points, String name) {
        displayElements = new ArrayList<>();
        this.name = name;
        this.points = points;
    }
    public QuestionContainer(QuestionContainerSpec qcs) {
        //type = qcs.type;
        name = qcs.name;
        if (qcs.points != 0)
            points = qcs.points;
        displayElements = new ArrayList<>();
        this.qcs = qcs;
    }
    private void writeQuestionOrAnswer(StringBuilder b, int questionNum, boolean isQuestion) {
        String className = isQuestion ? "question" : "answer";
        b.append("<div class='")
            .append(className)
            .append("'>\n");

        if (isQuestion) {
           b.append("<div>")
            .append(questionNum)
            .append(". ")
            .append(qcs.name)
            .append("<span class='pts'>  (")
            .append(qcs.points)
            .append(" points)</span>" +
                    "<input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>" +
                    "</div>\n")
            .append('\n');
        }

        for (DisplayElement d : displayElements) {
            d.writeHTML(b, isQuestion);
        }
        b.append("</div>\n\n");
    }
    public void writeHTML(StringBuilder b, int questionNum) {
        b.append("<div>\n");
        writeQuestionOrAnswer(b, questionNum, true);
        writeQuestionOrAnswer(b, questionNum, false);
        b.append("</div>\n");
    }
    public void addPoints(float points){
        this.points+=points;
    }
    public void add(DisplayElement e) {
        displayElements.add(e);
//        if(e instanceof Question){
            //this.points+=e.getPoints();
//        }
    }
}
