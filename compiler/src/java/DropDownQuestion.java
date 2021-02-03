public class DropDownQuestion extends QuestionType {
  private String answer, option, input;
  private String typeID = "q";
  private int count = 0;
  
  public void getAnswer() {
    input = "";
    for (int i = 0; i < answer.length(); i++) {
      if (answer[i] == '*') {
        for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
          input += answer[j];
        }
        answer.erase(i, 1);
      }
    }
  }
  
  public void getOptions() {
    replace = "(<select class='' name=')";
    replace += qID + "'" + "id='" + qID + "'>";
    for (int i = 0; i <= answer.length(); i++) {
      if (answer[i] == ',' || i == answer.length()) {
        replace += "("+
          "<option value=')" + option + "'>" + option + "</option>";
        option = "";
      } else {
        option += answer[i];
      }
    }
    replace += "("+
      "</select>)";
  }

  @Override 
  public String print(final LiquizCompiler compiler, ostream answerFile, int partNum, int questionsNum, double point) {
    input = "";
    answer = text.erase(0, 4);
    getAnswer();
    addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
    getOptions();
    answer = "";
    return replace;
  }
}