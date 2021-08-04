public class VerticalMultipleChoiceQuestion extends MultipleChoiceQuestion {

    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div class='vertical'>\n");
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

    public static VerticalMultipleChoiceQuestion lookUpPreDefined(int questionNumber, int partNumber, double points, Preferences prefs, String answerName, String answers) {
        String preDefinedAnswers = "";
        try {
            preDefinedAnswers = prefs.getDefinition(answerName);//TODO: put stars in next to right answers
        } catch (IllegalArgumentException e){

        }
        return new VerticalMultipleChoiceQuestion(questionNumber, partNumber, points, preDefinedAnswers);
    }

    public VerticalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, boolean multiAns, String answers) {
        super(questionNumber, partNumber, points, answers, multiAns);
    }

    public VerticalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, String answers) {
        super(questionNumber, partNumber, points, answers, false);
    }
}
