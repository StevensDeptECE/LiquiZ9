//package numquestion;
//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;
//import java.util.Enumeration;
//import java.util.ArrayList;
//import question.*;
//import numquestion.*;
//
//public class NumQuestion extends Question {
//  Double[] ansRange = new Double[2];
//  Double gradeVal;
//  String type;
//
//  public NumQuestion(Double lowRange, Double highRange, Double gradeVal){
//    this.ansRange = {lowRange, highRange};
//    this.gradeVal = gradeVal;
//    this.type = "NumQuestion";
//  }
//
//
//  public Double getGradeValue(){
//    return gradeVal;
//  }
//
//  public Double[] getAnswer(){
//    return ansRange;
//  }
//
//  public String getType(){
//    return type;
//  }
//
//  public Double checkAnswer(String studentAns){
//    Double numStudentAns =  Double.parseDouble(studentAns);
//    if(numStudentAns >= ansRange[0] && numStudentAns <= ansRange[1])
//      return 1.0*gradeVal;
//    return 0.0;
//  }
//}
