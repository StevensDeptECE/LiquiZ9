import java.util.ArrayList;

public abstract class MultipleChoiceQuestion extends Question {
    protected String[] choices;
    protected String[] answers;
    protected boolean multiAns;
    //TODO: This class has only what all multiplechoice questions have in common

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
        return "m_";
    }
    public MultipleChoiceQuestion(int questionNumber, int partNumber, double points, String answers, boolean multiAns) {
        super(questionNumber, partNumber, points);
        this.choices = answers.replace("*", "").split(",");
        this.multiAns = multiAns;
        ArrayList<String> tempArr = new ArrayList<String>();
        for(String ans : answers.split(",")) {
            if(ans.contains("*")){
                tempArr.add(ans.substring(1));
            }
        }
        this.answers = new String[tempArr.size()];
        this.answers = tempArr.toArray(this.answers);

    }

}
