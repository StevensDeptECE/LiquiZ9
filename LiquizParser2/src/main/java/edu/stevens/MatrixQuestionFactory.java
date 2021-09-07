package edu.stevens;

import java.util.regex.Matcher;

public class MatrixQuestionFactory extends QuestionFactory{
    @Override
    public Question makeQuestion(LiquiZParser p, Matcher m) {
        String matrixElements = m.group(3);
        String[] dimensions = m.group(2).split(",");
        int rows = Character.getNumericValue(dimensions[0].charAt(1));
        int columns = Character.getNumericValue(dimensions[1].charAt(0));
        return new MatrixQuestion(p.getQuestionNumber(), p.getPartNumber(), 1, rows, columns, matrixElements); //TODO:Fix Points
    }
}

