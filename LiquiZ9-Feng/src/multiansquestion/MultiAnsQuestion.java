//package multiansquestion;
//import java.util.ArrayList;
//
//public class MultiAnsQuestion {
//  String type;
//  int numAns;
//  Double gradeVal;
//  Boolean caseSens;
//  ArrayList<String> answers = new ArrayList();
//
//  public MultiAnsQuestion(String ans[], Double gradeVal, Boolean case){
//    this.type = "MultiAnsQuestion";
//    this.gradeVal = gradeVal;
//    this.numAns = 0;
//    this.caseSens = case;
//
//    for(String a : ans){
//      answers.add(a);
//      this.numAns+=1;
//    }
//  }
//
//  public String getType(){
//    return type;
//  }
//
//  public double getGradeValue(){
//    return gradeVal;
//  }
//
//  public ArrayList<String> getAnswer(){
//    return answers;
//  }
//
//  public boolean getCaseSensitivity(){
//    return caseSens;
//  }
//
//  public int getNumberOfAnswers(){
//    return numAns;
//  }
//
//  public Double checkAnswer(String studentAns[]){
//    int i = 0;
//    Double finishedGradeVal = 0.0;
//    for(String a : answers){
//      String studentA = studentAns[i];
//      if(!caseSens){
//         = studentAns[i].toLowerCase();
//        a = a.toLowerCase();
//      }
//      if(lCaseStudentAns.compare(a)==0)
//        finishedGradeVal += (1/numAns)*gradeVal;
//    }
//    return finishedGradeVal;
//  }
//}
