package GradeQuiz;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GradeQuiz {
    //use Plan A, Use arraylist;
//    private ArrayList<String>answerlist = new ArrayList<>();
//    private ArrayList<String>gradelist = new ArrayList<>();
    private String[] answerlist;
    private String[] gradelist;

    public GradeQuiz(student student){
        /*  Plan A:
         *   use filereader to read local file
         *   I think it is not very safe to be able to read the local file of the manager
         *   So I use plan B.
         */
//        try {
//            Scanner s1 = new Scanner(new FileReader("answer.dat"));
//            int num = 1;
//            while (s1.hasNext()) {
//                    answerlist.add(s1.next());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            Scanner s2 = new Scanner(new FileReader("grade.dat"));
//            int num = 1;
//            while (s2.hasNext()) {
//                gradelist.add(s2.next());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /*  Plan B:
         *   use filereader to read local file
         *   I think it is not very safe to be able to read the local file of the manager
         *   So I use plan B.
         */
        answerlist = new String[]{"351", "4","4"};
        gradelist = new String[]{"40", "30","30"};

        //grade
        double grades = 0.0;
        for(int i = 0; i < student.getStuAnswer().size(); ++i){
            if(answerlist[i].equals(student.getStuAnswer().get(i))){
                grades += Double.parseDouble(gradelist[i]);
            }
            student.setGrades(grades);
        }
    }
}
