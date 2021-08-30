package edu.stevens;

import java.util.ArrayList;

public class MatrixQuestion extends Question{
    protected String[] matrixElements;
    protected String[] answers;
    protected int rows;
    protected int columns;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div><table>\n");
        for(int i=0; i<rows; i++) {
            b.append("<tr>\n");
            for(int j=0; j<columns; j++) {
                if (matrixElements[j+columns*i].contains("*") && isQuestion) {
                    b.append("<td style='text-align:center; padding:15px'><input type='number' style='width:50px'/></td>\n");
                }
                else {
                    b.append("<td style='text-align:center; padding:15px'>").append(matrixElements[j+columns*i].replaceFirst("_", "")).append("</td>\n");
                }
            }
            b.append("</tr>\n");
        }
        b.append("</table><div>\n");
    }
    public void buildAnswers(StringBuilder b) {
        b.append(getPoints());
        if(answers.length > 0) {
            b.append("\t").append(answers[0]);
            for (int i = 1; i < answers.length; i++) {
                b.append(",").append(answers[i]);
            }
        }
        b.append("\n");
    }
    public String getqIdType() {
        return "x_";
    }
    public MatrixQuestion(int questionNumber, int partNumber, double points, int rows, int columns, String matrixElements) {
        super(questionNumber, partNumber, points);
        this.matrixElements = matrixElements.split(",");
        this.rows = rows;
        this.columns = columns;
        ArrayList<String> tempArr = new ArrayList<String>();
        for(String ans : this.matrixElements) {
            if(ans.contains("*")){
                tempArr.add(ans.substring(1));
            }
        }
        this.answers = new String[tempArr.size()];
        this.answers = tempArr.toArray(this.answers);

    }
}
