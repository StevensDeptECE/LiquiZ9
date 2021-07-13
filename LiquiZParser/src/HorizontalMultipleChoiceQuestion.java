public class HorizontalMultipleChoiceQuestion extends MultipleChoiceQuestion {
    public void buildHTML(StringBuilder b) {
        for (int i = 0; i < answers.length; i++)
        b.append(answers[i]); //TODO: maket his decent format! displays all answers

    }
}
