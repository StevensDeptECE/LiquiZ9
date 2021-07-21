public class HorizontalMultipleChoiceQuestion extends MultipleChoiceQuestion {
    public void buildHTML(StringBuilder b, boolean isQuestion) {
        for (int i = 0; i < answers.length; i++)
        b.append(answers[i]); //TODO: maket his decent format! displays all answers

    }
    public HorizontalMultipleChoiceQuestion(int questionNumber, int partNumber) {
        super(questionNumber, partNumber);
    }
}
