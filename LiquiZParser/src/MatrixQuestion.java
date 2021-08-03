public class MatrixQuestion extends Question{
    protected String[] matrixElements;
    protected int rows;
    protected int columns;
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        int counter = 0;
        b.append("<div><table>\n");
        for(int i=0; i<rows; i++) {
            b.append("<tr>\n");
            for(int j=0; j<columns; j++) {
                if (matrixElements[counter].equals("_")) {
                    b.append("<td style='text-align:center; padding:15px'><input type='number' style='width:50px'/></td>\n");
                }
                else {
                    b.append("<td style='text-align:center; padding:15px'>").append(matrixElements[counter]).append("</td>\n");
                }
                counter++;
            }
            b.append("</tr>\n");
        }
        b.append("</table><div>\n");
    }
    public void buildAnswers(StringBuilder b) {

    }
    public MatrixQuestion(int questionNumber, int partNumber, int rows, int columns, String matrixElements) {
        super(questionNumber, partNumber);
        this.matrixElements = matrixElements.split(",");
        this.rows = rows;
        this.columns = columns;

    }
}
