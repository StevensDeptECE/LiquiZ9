package GradeQuiz;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Answer {
    private Map<Integer,String> answerlist = new HashMap<Integer, String>();;
    private boolean AllSet = false;
    public Answer() {
    }

    public void setAnswer(int numsOfQuestion) throws IOException {
        int num = 1;
        for(int j = 0; j < numsOfQuestion; ++j){
            byte[] b=new byte[1024];
            System.out.print("Please type in the answer of question#:"+num);
            System.in.read(b);
            String str=new String(b);
            System.out.println(str);
                AllSet = true;
            answerlist.put(num++,str);
        }
    }
    public Map<Integer, String> getAnswerlist(){
        return answerlist;
    }
    public void a(){
        for(int key : answerlist.keySet()){
            String value = answerlist.get(key);
            System.out.println(key + value);
        }
    }

    public static void main(String args[]) throws IOException {
        Answer a = new Answer();
        a.setAnswer(3);
        a.a();
    }
}
