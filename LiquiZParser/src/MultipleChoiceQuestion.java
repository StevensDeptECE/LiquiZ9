public abstract class MultipleChoiceQuestion extends Question {
    protected String[] answers;
    //TODO: This class has only what all multiplechoice questions have in common

    public void buildAnswers(StringBuilder b) {
        //TODO:
    }
    public MultipleChoiceQuestion(int questionNumber, int partNumber) {
        super(questionNumber, partNumber);
    }

}
