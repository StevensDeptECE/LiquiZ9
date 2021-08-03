public abstract class Question extends DisplayElement {
    protected String qId;
    protected double points;

    protected final String setQuestionId(){
        if(this.getClass() == EssayQuestion.class || this.getClass() == FillinQuestion.class) {
            return "q_";
        }
        else if(this.getClass() == VerticalMultipleChoiceQuestion.class || this.getClass() == HorizontalMultipleChoiceQuestion.class){
            return "m_";
        }
        else if(this.getClass() == NumberFillinQuestion.class){
            return "n_";
        }
        else {
            return "Q_";
        }
    }
    public String getQuestionId(boolean isQuestion) { return isQuestion ? qId : "a" + qId; }
    public abstract void buildHTML(StringBuilder b, boolean isQuestion);
    public abstract void buildAnswers(StringBuilder b);
    //public abstract String getqIdType(); use an abstract function or a new qId class?

    public final void writeEnclosingDiv(StringBuilder b) {
        b.append("<div>");
    }

    public final void writeHTML(StringBuilder b, boolean isQuestion) {
        writeEnclosingDiv(b);
        buildHTML(b, isQuestion);
        b.append("</div>");
    }
    public final void writeAnswers(StringBuilder b) {
        b.append(qId).append("\t");
        buildAnswers(b);
    }
    public final double getPoints() {
        return points;
    }
    public Question(int questionNumber, int partNumber) {
        qId = setQuestionId() + questionNumber + "_" + partNumber; //TODO change first letter to match Ethan's question code
        points = 0.0;
    }
    public Question(int questionNumber, int partNumber, double points) {
        qId = setQuestionId() + questionNumber + "_" + partNumber; //TODO change first letter to match Ethan's question code
        this.points = points;
    }

}
