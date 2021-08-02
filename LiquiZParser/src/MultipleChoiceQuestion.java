public abstract class MultipleChoiceQuestion extends Question {
    protected String[] choices;
    protected String[] answers;
    protected boolean multiAns;
    //TODO: This class has only what all multiplechoice questions have in common

    public void buildAnswers(StringBuilder b) {
        b.append("getPoints()").append("\t").append(answers[0]);//TODO: figure out how to get points
        for(int i = 1; i < answers.length; i++) {
                b.append(",").append(answers[i]);
        }
        b.append("\n");
    }
    public MultipleChoiceQuestion(int questionNumber, int partNumber, boolean multiAns, String[] choices, String[] answers) {
        super(questionNumber, partNumber);
        this.answers = answers;
        this.multiAns = multiAns;
        this.choices = choices;
    }

}
