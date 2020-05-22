package GradeQuiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class student {
    private String name, id, passwd;
    private boolean checkBox = false;
    private double grades = 0.0;
    private ArrayList<String>StuAnswer = new ArrayList<>();

    public void setName(String name){
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setPasswd(String passwd){
        this.passwd = passwd;
    }
    public void setGrades(double grades){
        this.grades = grades;
    }
    public void setStuAnswer( String answer){ this.StuAnswer.add(answer); }
    public void setCheckBox(String status){
        if(status.equals("on")){
            this.checkBox = true;
        }
    }
    public ArrayList<String> getStuAnswer(){return StuAnswer;}
    public double getGrades(){return grades;}
    public void print(){
        System.out.println(name + id +passwd + checkBox);
        for(String key : StuAnswer){
                System.out.println(key);

        }
    }
}
