public class HorizontalMultipleChoiceQuestion extends MultipleChoiceQuestion {

    public void buildHTML(StringBuilder b, boolean isQuestion) {
        b.append("<div class='horizontal'>\n");
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

    public static HorizontalMultipleChoiceQuestion lookUpPreDefined(int questionNumber, int partNumber, double points, Preferences prefs, String answerName, String answers) {
        String preDefinedAnswers = prefs.get(answerName);//TODO: put stars in next to right answers
        return new HorizontalMultipleChoiceQuestion(questionNumber, partNumber, points, preDefinedAnswers);
    }

    public HorizontalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, String answers, boolean multiAns) {
        super(questionNumber, partNumber, points, answers, multiAns);
    }

    public HorizontalMultipleChoiceQuestion(int questionNumber, int partNumber, double points, String answers) {
        super(questionNumber, partNumber, points, answers, false);
    }
}
