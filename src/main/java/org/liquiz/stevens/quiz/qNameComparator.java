package org.liquiz.stevens.quiz;

import java.util.Comparator;

public class qNameComparator implements Comparator<String> {

    @Override
    public int compare(String q1, String q2) {
        String[] q1Split = q1.split("_");
        String[] q2Split = q2.split("_");
        if(Integer.parseInt(q1Split[1])>Integer.parseInt(q2Split[1]))
            return 1;
        else if(Integer.parseInt(q1Split[1])<Integer.parseInt(q2Split[1]))
            return -1;
        else {
            if (Integer.parseInt(q1Split[2]) > Integer.parseInt(q2Split[2]))
                return 1;
            else
                return -1;
        }
    }
}