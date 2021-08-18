package edu.stevens;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class QuizSpec {
    public String course;
    public String desc;
    public String parent;
    public HashMap<String, Object> defaults;
    public HashMap<String, String[]> def;
    public HashMap<String, SchoolInformation> schoolInfo;
}

class SchoolInformation {
    String color;
    String logo;
}



